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

- Function handler: com.pega.launchpad.net.HttpRequestWithMappedResponseHeaders::send
- Input parameters:
  - **url (Text)** - The URL of the REST service to call
  - **method (Text)** - The HTTP method to use: POST, PUT, PATCH, DELETE
  - **body (Text)** - The JSON string of the request body to send
  - **headers (Text)** - Optional, a JSON object containing key-value pairs, each will be added as headers to the HTTP Request
- Output parameters:
  - **Type**: *[choose one of your application's case types]*
  - **Cardinality**: Single or Multiple, depending on what the REST service returns (single json object vs json array of objects)
  - Note: JSON Transform rule will be required
    
### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Same as the cardinality chosen for your Function Rule's output parameter
2. Add example JSON response:
- **System name**: any identifier you want
- **JSON sample**: ```{"responseHeaders":..json object with name=value json elements..},{"responseBody":..json object/array for the expected JSON response}```
- For example, a GET call to https://api.lyrics.ovh/v1/beatles/Yesterday would need to use a JSON sample like this to properly define the JSON transform:
```
{
   "responseBody":{
      "lyrics":"Yesterday, \nall my troubles seemed so far away,\r\nNow it looks as though they\u0027re here to stay,\r\nOh I believe in yesterday.\r\nSuddenly, \n\nI\u0027m not half the man I used to be,\n\nThere\u0027s a shadow hanging over me.\n\nOh yesterday came suddenly.\n\n\n\nWhy she had to go? \n\nI don\u0027t know she woldn\u0027t say.\n\nI said something wrong, \n\nnow I long for yesterday.\n\n\n\nYesterday, \n\nlove was such an easy game to play,\n\nNow I need a place to hide away,\n\nOh I believe in yesterday.\n\n\n\nWhy she had to go? \n\nI don\u0027t know she woldn\u0027t say.\n\nI said something wrong, \n\nnow I long for yesterday.\n\n\n\nYesterday, \n\nlove was such an easy game to play,\n\nNow I need a place to hide away,\n\nOh I believe in yesterday.\n\n\n\n\n\n(Thanks to Beatles4ever for correcting these lyrics)"
   },
   "responseHeaders":{
      "Server":"nginx/1.18.0",
      "Access-Control-Allow-Origin":"*",
      "ETag":"W/\"325-2IYNwg6VyIduC0mDNRUyapNrfN0\"",
      "Connection":"keep-alive",
      "Content-Length":"805",
      "Date":"Sat, 27 Jul 2024 01:18:19 GMT",
      "Content-Type":"application/json; charset\u003dutf-8",
      "X-Powered-By":"Express"
   }
}
```
3. Map your data:
- For headers, map values from **responseHeaders** embedded values to your case as needed
- For the response body itself, map values from **responseBody** list or embedded object to your case
  
