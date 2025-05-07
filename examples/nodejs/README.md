# NodeJS Functions

<!-- TOC -->
* [NodeJS Functions](#nodejs-functions)
  * [Calculator function](#calculator-function)
    * [NodeJS code info](#nodejs-code-info)
    * [Function rule configuration](#function-rule-configuration)
    * [Deployment](#deployment)
<!-- TOC -->

## Calculator function

This function performs basic mathematical operations \(addition, subtraction, multiplication, and division\).

### NodeJS code info

- **File**: `Calculator.mjs`
- **Function**: Calculator.handler

### Function rule configuration

- **Runtime**: NodeJS (e.g., 18)
- **Function handler**: Calculator.handler
- **Input parameters**:
  - **num1 (Integer\)**: number 1
  - **num2 (Integer\)**: number 2
- **Output parameters**:
  - **Type**: (the object type you want to map data into)
  - **Cardinality**: Single object

Create a JSON transform for the response, using this sample json, and reference it in the function rule configuration:

```
{
  "addition": 8,
  "subtraction": -2,
  "multiplication": 15,
  "division": 0.6
}
```

### Deployment

After a successful build, the `nodejs.examples.zip` file is produced under `build/distributions`.  

In Pega Launchpad:

1. Select **Runtime**: NodeJS  
2. Upload the zip file to **Code bundle**  
3. Set **Function handler** to `Calculator.handler`  
4. Configure the input and output parameters as described above
