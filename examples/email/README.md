# Email examples

<!-- TOC -->
* [Email examples](#email-examples)
  * [Retrieve Emails](#retrieve-emails)
    * [Java code info](#java-code-info)
    * [Function rule configuration](#function-rule-configuration)
    * [JSON Transform rule configuration](#json-transform-rule-configuration)
<!-- TOC -->

## Retrieve Emails

Simple example of retrieving email from pop or imap servers. Customize as desired.

### Java code info
- **Class**: [com.pega.launchpad.email.EmailRetriever](src/main/java/com/pega/launchpad/email/EmailRetriever.java)
- **Method**: retrieve

### Function rule configuration

- Function handler: com.pega.launchpad.email.EmailRetriever::retrieve
- Input parameters:
    - String host
    - String port
    - String user
    - String password
    - String protocol (imap, pop3, pop3s, etc)
    - int maxCount: maximum number of emails to retrieve
    - boolean mock: If true, will not attempt to connect to the email server, and will return a mocked response for testing
- Output parameters:
    - **Type**: *[choose one of your application's case types where you want to store the results]*
    - **Cardinality**: Single object
    - Note: JSON Transform rule will be required

### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Single object
2. Add example JSON response:
- **System name**: any identifier you want
- **JSON sample**: See below example
- For example, you need to use a JSON sample like this to properly define the JSON transform:
```
{
  "success": true,
  "errorMessage": "",
  "messages": [
    {
      "from": "[noreply@pega.com]",
      "subject": "mock",
      "body": "mock data",
      "attachments": [
        {
          "filename": "mock.txt",
          "base64Content": "bW9jaw=="
        }
      ]
    }
  ]
}
```
3. Map your data:
- Map values from the json to the appropriate fields in your case
  
