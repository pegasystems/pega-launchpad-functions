# Overview

Some sample code and configuration to see how to create your own functions in [Pega Launchpad](https://launchpad.io/). Use as examples or starter code for your own functions as needed.

# Examples

## Base64 Decode: decodes a base64 string

This method takes a base64 string and decodes it.

### Java code info:
- **Class**: LPSTBase64
- **Method**: decode

### Function rule configuration:

- Function handler: com.pega.lpst.LPSTBase64::decode
- Input parameters:
  - **inputText (Text)**
- Output parameters:
  - **Type**: Text

## Base64 Encode: encodes a string with base64

This method takes a plain string and encodes it in base64.

### Java code info:
- Class: LPSTBase64
- Method: encode

### Function rule configuration:
- Function handler: com.pega.lpst.LPSTBase64::encode
- Input parameters:
  - **inputText (Text)**
- Output parameters:
  - **Type**: Text

## Text To List: Parses a delimited string into a List of cases

This method takes a delimited string, and returns its tokens in a List of objects. You can use a JSON transform to map those tokens back into your application data as required.

### Java code info:
- **Class**: LPSTParser
- **Method**: textToListOfObjects

### Function rule configuration:
- Function handler: com.pega.lpst.LPSTParser::textToListOfObjects
- Input parameters:
  - **inputText (Text)**: the delimited string to parse
  - **delim (Text)**: optional - the delimiter that was used (default is ",")
- Output parameters:
  - **Type**: *[choose one of your application's case types, doesn't matter which one]*
  - **Cardinality**: Multiple
  - Note: JSON Transform rule will be required

### JSON Transform rule configuration:

1. Create JSON Transform rule with:
  - **Name**: the same name as your function (not required, just easier for author)
  - **Purpose**: Deserialize (JSON to Pega Object)
  - **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
  - **Top level structure**: Multiple record
2. Add source JSON data:
  - **System name**: any identifier you want
  - **JSON sample**: ```[{"Name":"foo"},{"Name":"bar"}]```
5. Map your data:
  - **Source field**: Name(STRING)
  - **Target field**: .Name

## CSV To List: Parses a CSV file content into a List of cases

This method takes the content of a CSV file (headers required), and returns a list of objects, where each object has a member field in the form **Field1**, **Field2**, .., **FieldN**, corresponding to each column in your CSV file. A maximum of 10 columns are supported. You can use a JSON transform rule to map those fields from each object back into your application object structure.

### Java code info:
- **Class**: LPSTParser
- **Method**: csvToListOfObjects

### Function rule configuration:
- Function handler: com.pega.lpst.LPSTParser::csvToListOfObjects
- Input parameters:
  - **csv (Text)**: csv file content to parse
- Output parameters:
  - **Type**: *[choose one of your application's case types, doesn't matter which one]*
  - **Cardinality**: Multiple
  - Note: JSON Transform rule will be required

### JSON Transform rule configuration:

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Multiple record
2. Add source JSON data:
- **System name**: any identifier you want
- **JSON sample**: A list with a **FieldN** element for each column in your CSV (up to 10 fields), like: ```[{"Field1":"foo","Field2":"foo","Field3":"foo"}]```
5. Map your data:
- **Source field**: Field1(STRING)
- **Target field**: .MyAppField
- **Source field**: Field2(STRING)
- **Target field**: .MySecondAppField
- **Source field**: Field3(STRING)
- **Target field**: .MyThirdAppField
- **(etc..)**: up to 10 source fields supported.
