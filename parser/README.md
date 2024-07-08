# Parser examples

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
