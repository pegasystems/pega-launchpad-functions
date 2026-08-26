# MCP client functions

These functions connect to remote MCP servers over Streamable HTTP. They can list tools and prompts, call tools, and render prompts. Only HTTP transports are accepted.

## Python code info

- **File**: `mcpclient.py`
- **Functions**: `list_tools`, `list_prompts`, `call_tool`, `get_prompt`

## Function rule configuration

Create one Function rule for each handler using the same deployment zip.

- **Runtime**: Python 3.12
- **Function handlers**:
  - `mcpclient.list_tools`
  - `mcpclient.list_prompts`
  - `mcpclient.call_tool`
  - `mcpclient.get_prompt`
- **Output parameters**:
  - **Type**: Text
  - **Cardinality**: Single value

All inputs are Text. The `config`, `auth`, and `arguments` inputs contain JSON text.

| Function | Input parameters |
| --- | --- |
| `list_tools` | `config`, `auth` (optional) |
| `list_prompts` | `config`, `auth` (optional) |
| `call_tool` | `config`, `auth` (optional), `tool_name`, `arguments` (optional) |
| `get_prompt` | `config`, `auth` (optional), `prompt_name`, `arguments` (optional) |

For portal agents, create the Automation and Tool rules in **Data-Portal**. Map `config` and `auth` to constant Text values when appropriate, pass the remaining values through as Tool inputs, and add the Tools to the agent as knowledge tools.

## MCP configuration

The `config` value follows the MCP server configuration format. Each server must use an `http` or `https` URL. With multiple servers, use FastMCP's namespaced component name, such as `weather_get_forecast`.

**Example `call_tool` input**:

```json
{
  "config": {
    "mcpServers": {
      "weather": {
        "url": "https://weather-api.example.com/mcp"
      }
    }
  },
  "tool_name": "weather_get_forecast",
  "arguments": {
    "city": "London"
  }
}
```

The `get_prompt` function uses `prompt_name` instead of `tool_name`. The `arguments` input defaults to an empty object. MCP tool errors remain in the returned result's `isError` field; connection and validation errors are raised to the caller.

## Authentication

Authentication is optional. Bearer tokens and OAuth 2.0 client credentials are supported; interactive browser-based OAuth is not supported.

**Bearer token**:

```json
{
  "type": "bearer",
  "token": "access-token"
}
```

**Client credentials**:

```json
{
  "type": "client_credentials",
  "client_id": "mcp-client-id",
  "client_secret": "mcp-client-secret",
  "scopes": ["tools:read", "tools:write"],
  "token_endpoint_auth_method": "client_secret_basic"
}
```

Supported token endpoint authentication methods are `client_secret_basic` and `client_secret_post`. Do not place secrets in source-controlled examples or logs.

## Build

From the repository root:

```text
gradlew :examples:python:mcpclient:build
```

The zip artifact is produced under `examples/python/mcpclient/build/distributions/mcpclient-x.y.z-SNAPSHOT.zip`.