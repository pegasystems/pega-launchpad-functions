# Python Functions

<!-- TOC -->
* [Python Functions](#python-functions)
  * [Calculator function](#calculator-function)
<!-- TOC -->

## Calculator function

This function performs basic mathematical operations \(addition, subtraction, multiplication, and division\).

### Python code info

- **File**: `Calculator.py`
- **Function**: handler

### Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: Calculator.handler
- **Input parameters**:
  - **num1 \(\Integer\)**: number 1
  - **num2 \(\Integer\)**: number 2
- **Output parameters**:
  - **Type**: Text

### Deployment

After a successful build, the `python.examples.zip` file is produced under `build/distributions`.  

In Pega Launchpad:

1. Select **Runtime**: Python 3.12  
2. Upload the zip file to **Code bundle**  
3. Set **Function handler** to `Calculator.handler`
4. Configure the input and output parameters as described above