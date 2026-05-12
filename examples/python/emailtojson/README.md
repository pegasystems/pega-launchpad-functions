# Email to JSON function

This function downloads an email file (`.eml` or Outlook `.msg`) from a signed URL and returns its headers, body, and attachment metadata as JSON. The format is auto-detected by magic bytes: files starting with `D0 CF 11 E0` are parsed as `.msg`, otherwise as `.eml`. Attachment bytes are not returned (only metadata and a SHA-256 hash).

## Python code info

- **File**: `EmailToJson.py`
- **Function**: `handler`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `EmailToJson.handler`
- **Input parameters**:
  - **url (Text)**: A signed `http(s)` URL pointing to the `.eml` or `.msg` file to parse
- **Output parameters**:
  - **Type**: (the object type you want to map data into)
  - **Cardinality**: Single object

**Limits**:
- 25 MB maximum download size
- 15 second HTTP timeout
- `http(s)` URLs only

**Example input**:

```json
{
  "url": "https://example.com/signed-url-to-message.eml"
}
```

Create a JSON transform for the response, using this sample json, and reference it in the function rule configuration:

```json
{
  "format": "eml",
  "headers": {
    "subject": "Welcome to Pega Launchpad",
    "from": [{"name": "Alice Sender", "email": "alice@example.com"}],
    "to": [{"name": "Bob Recipient", "email": "bob@example.com"}],
    "cc": [],
    "bcc": [],
    "reply_to": [],
    "date": "2026-05-01T10:15:30+00:00",
    "date_raw": "Fri, 1 May 2026 10:15:30 +0000",
    "message_id": "<abc123@example.com>",
    "in_reply_to": null,
    "references": null
  },
  "body": {
    "text": "Hello Bob, ...",
    "html": "<html><body>Hello Bob, ...</body></html>"
  },
  "attachments": [
    {
      "filename": "invoice.pdf",
      "content_type": "application/pdf",
      "size": 12345,
      "sha256": "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
      "content_id": null
    }
  ]
}
```

On failure, the function returns an error object:

```json
{ "error": "Failed to fetch message: ..." }
```

## Build

From the repository root:

```
gradlew :examples:python:emailtojson:build
```

The zip artifact is produced under `build/distributions/emailtojson-x.y.z-SNAPSHOT.zip`.
