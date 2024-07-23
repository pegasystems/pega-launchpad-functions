# Base64 functions

<!-- TOC -->
* [Base64 functions](#base64-examples)
  * [Decode a base64 string](#decode-a-base64-string)
  * [encode a string with base64](#encode-a-string-with-base64)
<!-- TOC -->

## Decode a base64 string

This method takes a base64 string and decodes it.

### Java code info
- **Class**: [com.pega.launchpad.base64.Base64](src/main/java/com/pega/launchpad/base64/Base64.java)
- **Method**: decode

### Function rule configuration

- Function handler: com.pega.launchpad.base64.Base64::decode
- Input parameters:
  - **text (Text)**
- Output parameters:
  - **Type**: Text

## encode a string with base64

This method takes a plain string and encodes it in base64.

### Java code info
- **Class**: [com.pega.launchpad.base64.Base64](src/main/java/com/pega/launchpad/base64/Base64.java)
- Method: encode

### Function rule configuration
- Function handler: com.pega.launchpad.base64.Base64::encode
- Input parameters:
  - **text (Text)**
- Output parameters:
  - **Type**: Text
