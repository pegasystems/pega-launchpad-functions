# Text examples

## Regular Expression: evaluate text against a regex

This method takes a regex string, and a text string, and sees if the pattern matches the text

### Java code info
- **Class**: [com.pega.launchpad.text.Text](src/main/java/com/pega/launchpad/text/Text.java)
- **Method**: regex

### Function rule configuration

- Function handler: com.pega.launchpad.text.Text::regex
- Input parameters:
  - **regex (Text)**: A regular expression [pattern](https://docs.oracle.com/en/java/javase/22/docs/api/java.base/java/util/regex/Pattern.html)
  - **text (Text)**: The text to evaluate
  - **caseInsensitive (Text)**: Optional - true or false
- Output parameters:
  - **Type**: Boolean: true if the text matches the regular expression pattern, false otherwise

## Format a java string with substitution values

This method is a wrapper for the java String.format() method, to substitute values into a string and return the formatted string.

### Java code info
- **Class**: [com.pega.launchpad.text.Text](src/main/java/com/pega/launchpad/text/Text.java)
- Method: setFields

### Function rule configuration
- Function handler: com.pega.launchpad.text.Text::format
- Input parameters:
  - **format (Text)**: a valid java String [format](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Formatter.html#syntax) like ```Please contact %s for details, or %s for other questions```
  - **values (Text)**: a comma-delimited string of values to use, like ```Tim,Gabe```
- Output parameters:
  - **Type**: Text: The formatted String. For the example inputs above, the result would be ```Please contact Tim for details, or Gabe for other questions```
