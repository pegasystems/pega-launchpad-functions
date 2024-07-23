# Net examples

<!-- TOC -->
* [Net examples](#net-examples)
  * [Send an HTTP Request](#Send-an-HTTP-Request)
<!-- TOC -->

## Send an HTTP Request

Send an HTTP request, and map the response body and headers into a Map that can be processed by a JSON transform.

### Java code info
- **Class**: [com.pega.launchpad.net.HttpRequestWithMappedResponseHeaders](src/main/java/com/pega/launchpad/net/HttpRequestWithMappedResponseHeaders.java)
- **Method**: send

### Function rule configuration

- Function handler: com.pega.launchpad.net.HttpRequestWithMappedResponseHeader::send
- Input parameters:
  - **url (Text)** - The URL of the REST service to call
  - **method (Text)** - The HTTP method to use: POST, PUT, PATCH, DELETE
  - **body (Text)** - The JSON string of the request body to send
  - **headers (Text)** - Optional, a JSON object containing key-value pairs, each will be added as headers to the HTTP Request
- Output parameters:
  - **Type**: *[choose one of your application's case types]*
  - **Cardinality**: Single
  - Note: JSON Transform rule will be required
    
### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Single
2. Add example JSON response:
- **System name**: any identifier you want
- **JSON sample**: ```{"responseHeaders":..json object with headers..},{"responseBody":..json object for the expected JSON response}```
3. Map your data:
- For headers, map values from **responseHeaders** embedded values to your case as needed
- For the response body itself, map values from **responseBody** embedded objects to your case
  
