# Parser functions

<!-- TOC -->
* [Parser functions](#parser-functions)
  * [Text To List: Parses a delimited string into a List of cases](#text-to-list-parses-a-delimited-string-into-a-list-of-cases)
  * [CSV To List: Parses a CSV file content into a List of cases](#csv-to-list-parses-a-csv-file-content-into-a-list-of-cases)
  * [json: deserialize a json string with a single top-level object](#json-deserialize-a-json-string-with-a-single-top-level-object)
  * [json deserialize a json string with a top-level array of objects](#json-deserialize-a-json-string-with-a-top-level-array-of-objects)
<!-- TOC -->

## Text To List: Parses a delimited string into a List of cases

This method takes a delimited string, and returns its tokens in a List of Map<String,String> objects. You can use a JSON transform to map those tokens back into your application data as required.

### Java code info
- **Class**: [com.pega.launchpad.parser.Parser](src/main/java/com/pega/launchpad/parser/Parser.java)
- **Method**: fromDelimitedText

### Function rule configuration
- Function handler: com.pega.launchpad.parser.Parser::fromDelimitedText
- Input parameters:
  - **text (Text)**: the delimited string to parse
  - **delim (Text)**: optional - the delimiter that was used (default is ",")
- Output parameters:
  - **Type**: *[choose one of your application's case types, doesn't matter which one]*
  - **Cardinality**: Multiple
  - Note: JSON Transform rule will be required

### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Multiple record
2. Add source JSON data:
- **System name**: any identifier you want
- **JSON sample**: ```[{"token":"foo"},{"token":"bar"}]```
5. Map your data:
- **Source field**: token(STRING)
- **Target field**: Your application field

## CSV To List: Parses a CSV file content into a List of cases

This method takes the content of a CSV file (headers required), and returns a list of Map<?,?> objects, where each object has member fields where the field name is the column name from your CSV header, and the value is the value for that column for that record. You must use a JSON Transform to map this list of objects back into your application object structure.

### Java code info
- **Class**: [com.pega.launchpad.parser.Parser](src/main/java/com/pega/launchpad/parser/Parser.java)
- **Method**: fromCsv

### Function rule configuration
- Function handler: com.pega.launchpad.parser.Parser::fromCsv
- Input parameters:
  - **csv (Text)**: csv file content to parse
- Output parameters:
  - **Type**: *[choose one of your application's case types, doesn't matter which one]*
  - **Cardinality**: Multiple
  - Note: JSON Transform rule will be required

### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Multiple record
2. Add source JSON data:
- **System name**: any identifier you want
- **JSON sample**: a json array with one element: a json object representing one record in your csv, with name:value pairs corresponding to the column names and record values. Example: ```[{"name":"tim","city":"Waltham","state":"MA"}]```
3. Map your data. Using the above name/city/state structure as an example:
- **Source field**: name(STRING)
- **Target field**: .CustomerName
- **Source field**: city(STRING)
- **Target field**: .CustomerCity
- **Source field**: state(STRING)
- **Target field**: .CustomerState

## json: deserialize a json string with a single top-level object

This method takes a json string, containing a single top-level object, and returns a TreeMap. The returned map can be processed by a JSON Transform to map values back into your application data structure.

### Java code info
- **Class**: [com.pega.launchpad.parser.Parser](https://github.com/miratim/PegaLPSTTools/blob/master/src/main/java/com/pega/launchpad/parser/Parser.java)
- **Method**: fromJsonObject

### Function rule configuration

- Function handler: com.pega.launchpad.parser.Parser::fromJsonObject
- Input parameters:
  - **json (Text)**: The json string to evaluate
- Output parameters:
  - **Type**: *[choose the application case type you want to map data into]*
  - **Cardinality**: Single
  - Note: JSON Transform rule will be required

### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Single record
2. Add source JSON data:
- **System name**: any identifier you want
- **JSON sample**: ```(example json structure from your use case)```
3. Map your data from the JSON structure into your application object structure

## json deserialize a json string with a top-level array of objects

This method takes a json string, containing a top-level array of objects, and returns a List of TreeMap objects. The returned maps can be processed by a JSON Transform to map values back into your application data structure.

### Java code info
- **Class**: [com.pega.launchpad.parser.Parser](https://github.com/miratim/PegaLPSTTools/blob/master/src/main/java/com/pega/launchpad/parser/Parser.java)
- **Method**: fromJsonArray

### Function rule configuration

- Function handler: com.pega.launchpad.parser.Parser::fromJsonArray
- Input parameters:
  - **json (Text)**: The json string to evaluate
- Output parameters:
  - **Type**: *[choose the application case type you want to map data into]*
  - **Cardinality**: Multiple
  - Note: JSON Transform rule will be required

### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Multiple records
2. Add source JSON data:
- **System name**: any identifier you want
- **JSON sample**: ```(example json structure from your use case)```
3. Map your data from the JSON structure into a multi-object field in your application object structure  
