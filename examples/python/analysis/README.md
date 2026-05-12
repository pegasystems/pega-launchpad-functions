# Analysis function

This function takes the URL for a CSV file as input, reads the data, and identifies the minimum and maximum voltage events. The sample CSV file can be found [here](./resources/voltagedata.csv).

## Python code info

- **File**: `Analysis.py`
- **Function**: `handler`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `Analysis.handler`
- **Input parameters**:
    - **SignedURL (Text)**: the URL of the CSV file to analyze. For attachments in launchpad, you need to generate a signed URL
- **Output parameters**:
    - **Type**: (the object type you want to map data into)
    - **Cardinality**: Single object

Create a JSON transform for the response, using this sample json, and reference it in the function rule configuration:

```json
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

## Additional configuration

To exercise this in launchpad, you can:
1. Add an attachment field to your case type
2. Add a task shape where user uploads the CSV file
3. Create an automation that is invoked after that task shape, and:
   1. Gets a signed url for that attachment: `Attachment@Function:GetFileAttachmentContent(Primary.InputFile.ID,"URL")`
   2. Invokes the function with that signed url as input
   3. Maps the output from the object returned by the function to fields in the case

## Build

From the repository root:

```
gradlew :examples:python:analysis:build
```

The zip artifact is produced under `build/distributions/analysis-x.y.z-SNAPSHOT.zip`.
