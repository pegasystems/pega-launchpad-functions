import asyncio
import json
from copy import deepcopy
from urllib.parse import urlparse


HTTP_TRANSPORT_NAMES = {"http", "streamable-http", "streamable_http"}


def list_tools(event, context):
    """List MCP tools using an HTTP-only MCP configuration."""
    request = _request_object(event)
    config = _http_config(request.get("config"))
    auth = _build_auth(request.get("auth"))

    return asyncio.run(_list_tools(config, auth))


def list_prompts(event, context):
    """List MCP prompts using an HTTP-only MCP configuration."""
    request = _request_object(event)
    config = _http_config(request.get("config"))
    auth = _build_auth(request.get("auth"))

    return asyncio.run(_list_prompts(config, auth))


def call_tool(event, context):
    """Call an MCP tool using an HTTP-only MCP configuration."""
    request = _request_object(event)
    config = _http_config(request.get("config"))
    tool_name = _required_string(request, "tool_name")
    arguments = _optional_object(request.get("arguments"), "arguments")
    auth = _build_auth(request.get("auth"))

    return asyncio.run(_call_tool(config, tool_name, arguments, auth))


def get_prompt(event, context):
    """Get a rendered MCP prompt using an HTTP-only MCP configuration."""
    request = _request_object(event)
    config = _http_config(request.get("config"))
    prompt_name = _required_string(request, "prompt_name")
    arguments = _optional_object(request.get("arguments"), "arguments")
    auth = _build_auth(request.get("auth"))

    return asyncio.run(_get_prompt(config, prompt_name, arguments, auth))


async def _list_tools(config, auth):
    client_class, _, _ = _import_fastmcp()
    client = _create_client(client_class, config, auth)
    async with client:
        tools = await client.list_tools()
    return _json_compatible(tools)


async def _list_prompts(config, auth):
    client_class, _, _ = _import_fastmcp()
    client = _create_client(client_class, config, auth)
    async with client:
        prompts = await client.list_prompts()
    return _json_compatible(prompts)


async def _call_tool(config, tool_name, arguments, auth):
    client_class, _, _ = _import_fastmcp()
    client = _create_client(client_class, config, auth)
    async with client:
        result = await client.call_tool(tool_name, arguments)
    return _json_compatible(result)


async def _get_prompt(config, prompt_name, arguments, auth):
    client_class, _, _ = _import_fastmcp()
    client = _create_client(client_class, config, auth)
    async with client:
        result = await client.get_prompt(prompt_name, arguments)
    return _json_compatible(result)


def _create_client(client_class, config, auth):
    if auth is None:
        return client_class(config)
    return client_class(config, auth=auth)


def _import_fastmcp():
    from fastmcp import Client
    from fastmcp.client.auth import BearerAuth

    try:
        from fastmcp.client.auth import ClientCredentialsOAuthProvider
    except ImportError:
        ClientCredentialsOAuthProvider = None

    return Client, BearerAuth, ClientCredentialsOAuthProvider


def _build_auth(auth_config):
    if auth_config is None:
        return None
    if not isinstance(auth_config, dict):
        raise ValueError("auth must be an object")

    auth_type = auth_config.get("type")
    if auth_type not in {"bearer", "client_credentials"}:
        raise ValueError("auth.type must be 'bearer' or 'client_credentials'")

    if auth_type == "bearer":
        _, bearer_auth, _ = _import_fastmcp()
        token = auth_config.get("token") or auth_config.get("access_token")
        if not isinstance(token, str) or not token:
            raise ValueError("Bearer auth requires a non-empty 'token'")
        return bearer_auth(token)

    _, _, client_credentials = _import_fastmcp()
    if client_credentials is None:
        raise ImportError(
            "The installed FastMCP client does not provide "
            "ClientCredentialsOAuthProvider"
        )
    client_id = auth_config.get("client_id")
    client_secret = auth_config.get("client_secret")
    if not isinstance(client_id, str) or not client_id:
        raise ValueError("Client credentials auth requires 'client_id'")
    if not isinstance(client_secret, str) or not client_secret:
        raise ValueError("Client credentials auth requires 'client_secret'")

    provider_options = {
        "client_id": client_id,
        "client_secret": client_secret,
    }
    if "scopes" in auth_config:
        provider_options["scopes"] = auth_config["scopes"]
    if "token_endpoint_auth_method" in auth_config:
        token_endpoint_auth_method = auth_config["token_endpoint_auth_method"]
        if token_endpoint_auth_method not in {
            "client_secret_basic",
            "client_secret_post",
        }:
            raise ValueError(
                "token_endpoint_auth_method must be "
                "'client_secret_basic' or 'client_secret_post'"
            )
        provider_options["token_endpoint_auth_method"] = (
            token_endpoint_auth_method
        )
    return client_credentials(**provider_options)


def _request_object(request):
    if isinstance(request, str):
        try:
            request = json.loads(request)
        except json.JSONDecodeError as exc:
            raise ValueError("request must contain valid JSON") from exc
    if not isinstance(request, dict):
        raise ValueError("request must be an object")
    return request


def _http_config(config):
    if isinstance(config, str):
        try:
            config = json.loads(config)
        except json.JSONDecodeError as exc:
            raise ValueError("config must contain valid JSON") from exc
    if not isinstance(config, dict):
        raise ValueError("config must be an object")

    servers = config.get("mcpServers")
    if not isinstance(servers, dict) or not servers:
        raise ValueError("config.mcpServers must be a non-empty object")

    normalized_config = deepcopy(config)
    normalized_servers = normalized_config["mcpServers"] = {}
    for server_name, server in servers.items():
        if not isinstance(server_name, str) or not server_name:
            raise ValueError("mcpServers names must be non-empty strings")
        if not isinstance(server, dict):
            raise ValueError(f"mcpServers.{server_name} must be an object")
        if "command" in server or "args" in server:
            raise ValueError(
                f"mcpServers.{server_name} must use an HTTP url, not a command"
            )

        url = server.get("url")
        parsed_url = urlparse(url) if isinstance(url, str) else None
        if parsed_url is None or parsed_url.scheme not in {"http", "https"}:
            raise ValueError(
                f"mcpServers.{server_name}.url must be an http(s) URL"
            )
        if not parsed_url.netloc:
            raise ValueError(f"mcpServers.{server_name}.url is invalid")

        transport = server.get("transport")
        if transport is not None and transport not in HTTP_TRANSPORT_NAMES:
            raise ValueError(
                f"mcpServers.{server_name}.transport must be an HTTP transport"
            )

        normalized_server = dict(server)
        normalized_server["transport"] = "http"
        normalized_servers[server_name] = normalized_server

    return normalized_config


def _required_string(request, field_name):
    value = request.get(field_name)
    if not isinstance(value, str) or not value:
        raise ValueError(f"{field_name} must be a non-empty string")
    return value


def _optional_object(value, field_name):
    if value is None:
        return {}
    if isinstance(value, str):
        try:
            value = json.loads(value)
        except json.JSONDecodeError as exc:
            raise ValueError(f"{field_name} must contain valid JSON") from exc
    if not isinstance(value, dict):
        raise ValueError(f"{field_name} must be an object")
    return value


def _json_compatible(value):
    if value is None or isinstance(value, (str, int, float, bool)):
        return value
    if isinstance(value, dict):
        return {str(key): _json_compatible(item) for key, item in value.items()}
    if isinstance(value, (list, tuple)):
        return [_json_compatible(item) for item in value]
    if hasattr(value, "model_dump"):
        return _json_compatible(value.model_dump(mode="json"))
    if hasattr(value, "dict"):
        return _json_compatible(value.dict())
    if hasattr(value, "__dict__"):
        return _json_compatible(vars(value))

    try:
        json.dumps(value)
    except TypeError as exc:
        raise TypeError(
            f"MCP response contains unsupported value: {type(value).__name__}"
        ) from exc
    return value