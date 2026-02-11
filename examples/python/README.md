# Python Functions

<!-- TOC -->
* [Python Functions](#python-functions)
  * [Managing Dependencies](#managing-dependencies)
  * [Calculator function](#calculator-function)
    * [Python code info](#python-code-info)
    * [Function rule configuration](#function-rule-configuration)
    * [Deployment](#deployment)
<!-- TOC -->

## Managing Dependencies

This project automatically includes Python package dependencies in the build output.

### requirements.txt

The `requirements.txt` file specifies all Python packages required by the functions. When you build the project, pip will download these dependencies and include them in the final zip file.

To add a new dependency:
1. Add the package to `requirements.txt` (e.g., `requests>=2.25.0`)
2. Run the build - dependencies will be automatically downloaded and included

Current dependencies:
- requests: For making HTTP requests

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

## Analysis function

This function takes the URL for a CSV file as input, reads the data, and identifies the minimum and maximum voltage events. The sample CSV file can be found [here](./resources/voltagedata.csv)  

### Python code info

- **File**: `Analysis.py`
- **Function**: handler

### Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: Calculator.handler
- **Input parameters**:
    - **SignedURL \(\Text\)**: the URL of the CSV file to analyze. For attachments in launchpad, you need to generate a signed URL
- **Output parameters**:
    - **Type**: (the object type you want to map data into)
    - **Cardinality**: Single object

Create a JSON transform for the response, using this sample json, and reference it in the function rule configuration:

```
{
  "max_voltage_row": {
    "datetime": "2025-12-14 06:03:20",
    "cellname": "95d09075-bc10-4680-a669-a44543ae5bc7",
    "voltage": 12.9999
  },
  "min_voltage_row": {
    "datetime": "2025-08-30 18:26:33",
    "cellname": "312145e2-b452-4e84-918e-8d40c2b5609e",
    "voltage": 0.0003
  }
}
```

### Additional configuration

To exercise this in launchpad, you can:
1. Add an attachment field to your case type
2. Add a task shape where user uploads the CSV file
3. Create an automation that is invoked after that task shape, and:
   1. Gets a signed url for that attachment: ```Attachment@Function:GetFileAttachmentContent(Primary.InputFile.ID,"URL")```
   2. Invokes the function with that signed url as input 
   3. Maps the output from the object returned by the function to fields in the case

### Deployment

After a successful build, the `python-x.x.x-SNAPSHOT.zip` file is produced under `build/distributions`. This zip file includes:
- All Python source code files
- Resource files
- All dependencies specified in requirements.txt

In Pega Launchpad:

1. Select **Runtime**: Python 3.12  
2. Upload the zip file to **Code bundle**  
3. Set **Function handler** to either `Calculator.handler` or `Analysis.handler` depending on which example you want to try 
4. Configure the input and output parameters as described above