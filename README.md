# Overview

Some sample code and configuration to see how to create your own functions in Pega Launchpad. Use as examples or starter code for your own functions as needed.

# Examples

## Base64 Decode: decodes a base64 string

### Java code info:
- Class: LPSTBase64
- Method: decode

### Function rule configuration:

- Function handler: com.pega.lpst.LPSTBase64::decode
- Input parameters:
  - inputText (Text)
- Output parameters:
  - Type: Text

## Base64 Encode: encodes a string with base64

### Java code info:
- Class: LPSTBase64
- Method: encode

### Function rule configuration:
- Function handler: com.pega.lpst.LPSTBase64::encode
- Input parameters:
  - inputText (Text)
- Output parameters:
  - Type: Text

## Text To List: Parses a delimited string into a List of cases

### Java code info:
- Class: LPSTParser
- Method: textToListOfObjects

### Function rule configuration:
- Function handler: com.pega.lpst.LPSTParser::textToListOfObjects
- Input parameters:
  - inputText (Text), the delimited string to parse
  - optional: delim (Text), the delimiter that was used (default is ",")
- Output parameters:
  - Type: *[choose one of your app case types]*
  - Cardinality: Multiple 
  - JSON Transform rule will be required

### JSON Transform rule configuration:

1. Create JSON Transform rule with:
   2. **Name**: the same name as your function (not required, just easier for author)
   3. **Purpose**: Deserialize (JSON to Pega Object)
   4. **Library**: Any case type from your application (doesn't matter which one)
   5. **Top level structure**: Multiple record
2. Add source JSON data:
   3. **System name**: any identifier you want
   4. **JSON sample**: ```[{"Name":"foo"},{"Name":"bar"}]``` 
5. Map your data:
   6. **Source field**: Name(STRING)
   7. **Target field**: .Name
   

