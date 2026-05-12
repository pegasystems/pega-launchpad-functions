import email
import hashlib
import urllib.request
from email import policy
from email.header import decode_header, make_header
from email.utils import getaddresses, parsedate_to_datetime

MAX_BYTES = 25 * 1024 * 1024  # 25 MB cap to protect Lambda memory
FETCH_TIMEOUT = 15  # seconds
OLE_MAGIC = b"\xd0\xcf\x11\xe0\xa1\xb1\x1a\xe1"  # .msg / Compound File Binary


def handler(event, context):
    url = event.get("url")
    if not url:
        return {"error": "Missing required field: 'url'"}

    if not (url.startswith("https://") or url.startswith("http://")):
        return {"error": "URL must be http(s)"}

    try:
        data = _fetch(url)
    except Exception as exc:
        return {"error": f"Failed to fetch message: {exc}"}

    try:
        if data.startswith(OLE_MAGIC):
            return _parse_msg(data)
        return _parse_eml(data)
    except Exception as exc:
        return {"error": f"Failed to parse message: {exc}"}


def _fetch(url):
    req = urllib.request.Request(url, headers={"User-Agent": "pega-launchpad-email-to-json/1.0"})
    with urllib.request.urlopen(req, timeout=FETCH_TIMEOUT) as resp:
        data = resp.read(MAX_BYTES + 1)
    if len(data) > MAX_BYTES:
        raise ValueError(f"Message exceeds maximum size of {MAX_BYTES} bytes")
    return data


# ---------- EML ----------

def _parse_eml(data):
    msg = email.message_from_bytes(data, policy=policy.default)
    text_body, html_body, attachments = _walk_eml(msg)
    return {
        "format": "eml",
        "headers": _eml_headers(msg),
        "body": {"text": text_body, "html": html_body},
        "attachments": attachments,
    }


def _eml_headers(msg):
    date_iso = None
    raw_date = msg.get("Date")
    if raw_date:
        try:
            date_iso = parsedate_to_datetime(raw_date).isoformat()
        except Exception:
            date_iso = None

    return {
        "subject": _decode(msg.get("Subject")),
        "from": _addresses(msg.get_all("From")),
        "to": _addresses(msg.get_all("To")),
        "cc": _addresses(msg.get_all("Cc")),
        "bcc": _addresses(msg.get_all("Bcc")),
        "reply_to": _addresses(msg.get_all("Reply-To")),
        "date": date_iso,
        "date_raw": raw_date,
        "message_id": msg.get("Message-ID"),
        "in_reply_to": msg.get("In-Reply-To"),
        "references": msg.get("References"),
    }


def _decode(value):
    if value is None:
        return None
    try:
        return str(make_header(decode_header(value)))
    except Exception:
        return value


def _addresses(values):
    if not values:
        return []
    parsed = getaddresses(values)
    result = []
    for name, addr in parsed:
        if not name and not addr:
            continue
        result.append({"name": _decode(name) or None, "email": addr or None})
    return result


def _walk_eml(msg):
    text_parts = []
    html_parts = []
    attachments = []

    for part in msg.walk():
        if part.is_multipart():
            continue

        content_type = part.get_content_type()
        disposition = (part.get_content_disposition() or "").lower()
        filename = part.get_filename()
        if filename:
            filename = _decode(filename)

        if disposition == "attachment" or (filename and content_type not in ("text/plain", "text/html")):
            payload = part.get_payload(decode=True) or b""
            attachments.append({
                "filename": filename,
                "content_type": content_type,
                "size": len(payload),
                "sha256": hashlib.sha256(payload).hexdigest() if payload else None,
                "content_id": (part.get("Content-ID") or "").strip("<>") or None,
            })
            continue

        if content_type == "text/plain":
            text_parts.append(_get_text(part))
        elif content_type == "text/html":
            html_parts.append(_get_text(part))

    return (
        "\n".join(p for p in text_parts if p) or None,
        "\n".join(p for p in html_parts if p) or None,
        attachments,
    )


def _get_text(part):
    try:
        return part.get_content()
    except Exception:
        payload = part.get_payload(decode=True) or b""
        charset = part.get_content_charset() or "utf-8"
        try:
            return payload.decode(charset, errors="replace")
        except LookupError:
            return payload.decode("utf-8", errors="replace")


# ---------- MSG (Outlook) ----------

def _parse_msg(data):
    import io
    import extract_msg  # pure-Python, deps: olefile, compressed-rtf, RTFDE, tzlocal

    with extract_msg.openMsg(io.BytesIO(data)) as m:
        raw_date = m.date
        date_iso = None
        try:
            if hasattr(raw_date, "isoformat"):
                date_iso = raw_date.isoformat()
        except Exception:
            pass

        attachments = []
        for att in (m.attachments or []):
            payload = att.data if isinstance(att.data, (bytes, bytearray)) else b""
            attachments.append({
                "filename": att.longFilename or att.shortFilename,
                "content_type": getattr(att, "mimetype", None),
                "size": len(payload),
                "sha256": hashlib.sha256(payload).hexdigest() if payload else None,
                "content_id": getattr(att, "cid", None) or None,
            })

        return {
            "format": "msg",
            "headers": {
                "subject": m.subject,
                "from": _msg_addresses(m.sender),
                "to": _msg_addresses(m.to),
                "cc": _msg_addresses(m.cc),
                "bcc": _msg_addresses(m.bcc),
                "reply_to": [],
                "date": date_iso,
                "date_raw": str(raw_date) if raw_date else None,
                "message_id": m.messageId,
                "in_reply_to": getattr(m, "inReplyTo", None),
                "references": None,
            },
            "body": {
                "text": m.body or None,
                "html": _msg_html(m),
            },
            "attachments": attachments,
        }


def _msg_html(m):
    html = getattr(m, "htmlBody", None)
    if isinstance(html, bytes):
        try:
            return html.decode("utf-8", errors="replace")
        except Exception:
            return None
    return html or None


def _msg_addresses(value):
    if not value:
        return []
    # extract_msg returns a single string like:
    #   '"Doe; John" <john@a>; "Smith; Jane" <jane@b>'
    # Display names can contain ';' or ',' inside quotes, so split on ';'
    # only at the top level (outside quoted strings).
    parts = _split_top_level(value, ";")
    parsed = getaddresses(parts)
    result = []
    for name, addr in parsed:
        if not name and not addr:
            continue
        result.append({"name": name or None, "email": addr or None})
    return result


def _split_top_level(value, sep):
    out = []
    buf = []
    in_quote = False
    in_angle = 0
    for ch in value:
        if ch == '"' and not in_angle:
            in_quote = not in_quote
            buf.append(ch)
        elif ch == "<" and not in_quote:
            in_angle += 1
            buf.append(ch)
        elif ch == ">" and not in_quote and in_angle:
            in_angle -= 1
            buf.append(ch)
        elif ch == sep and not in_quote and not in_angle:
            piece = "".join(buf).strip()
            if piece:
                out.append(piece)
            buf = []
        else:
            buf.append(ch)
    piece = "".join(buf).strip()
    if piece:
        out.append(piece)
    return out
