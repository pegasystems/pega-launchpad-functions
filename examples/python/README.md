# Python Functions

<!-- TOC -->
* [Python Functions](#python-functions)
  * [Calculator function](#calculator-function)
    * [Python code info](#python-code-info)
    * [Function rule configuration](#function-rule-configuration)
  * [Analysis function](#analysis-function)
    * [Python code info](#python-code-info-1)
    * [Function rule configuration](#function-rule-configuration-1)
    * [Additional configuration](#additional-configuration)
  * [Format currency function](#format-currency-function)
    * [Python code info](#python-code-info-2)
    * [Function rule configuration](#function-rule-configuration-2)
  * [Mongo writer function](#mongo-writer-function)
    * [Python code info](#python-code-info-3)
    * [Function rule configuration](#function-rule-configuration-3)
    * [CSV file examples](#csv-file-examples)
  * [Mongo Analysis function](#mongo-analysis-function)
    * [Python code info](#python-code-info-4)
    * [Function rule configuration](#function-rule-configuration-4)
  * [Deployment](#deployment)
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
  - **num1 (Integer)**: number 1
  - **num2 (Integer)**: number 2
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
    - **SignedURL (Text)**: the URL of the CSV file to analyze. For attachments in launchpad, you need to generate a signed URL
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

## Format currency function

This function formats a numeric amount as a localized currency string using the [Babel](https://github.com/python-babel/babel) library. It supports multiple currencies and locales for proper currency symbol and number formatting.

### Python code info

- **File**: `Babel.py`
- **Function**: format_currency

### Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: Babel.format_currency
- **Input parameters**:
  - **amount (Decimal)**: The numeric value to format as currency
  - **currency (Text)**: The ISO 4217 currency code (e.g., `USD`, `EUR`, `GBP`, `JPY`). Optional, defaults to `USD`
  - **locale (Text)**: The locale string for formatting (e.g., `en_US`, `fr_FR`, `de_DE`). Optional, defaults to `en_US`
- **Output parameters**:
  - **Type**: Text
  - **Cardinality**: Single value

The function returns a formatted currency string directly (e.g., `$1,234.56`, `€1,234.56`).

**Example outputs**:

```
USD: $1,234.56
EUR: €1,234.56
GBP: £1,234.56
JPY: ¥1,235
CHF: CHF 1,234.56
```

**Example inputs**:

```json
{
  "amount": 1234.56,
  "currency": "USD"
}
```

```json
{
  "amount": 1234.56,
  "currency": "EUR",
  "locale": "fr_FR"
}
```

```json
{
  "amount": 1234.56,
  "currency": "JPY"
}
```

## Mongo writer function

This function provides MongoDB operations for inserting single documents or performing bulk write operations using the pymongo driver. It uses a `w=0` (unacknowledged) write concern for maximum write throughput.

### Python code info

- **File**: `MongoWriter.py`
- **Function**: handler

### Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: MongoWriter.handler
- **Input parameters**:
  - **connection_string (Text)**: MongoDB connection string with embedded credentials (e.g., `mongodb+srv://username:password@host/?appName=demo`)
  - **operation (Text)**: Operation type - either `insert_one` or `bulk_write`
  - **namespace (Text)**: Database and collection in format `database.collection`
  - **document (Object)**: Single document for `insert_one` operation (required when operation is `insert_one`)
  - **csv_url (Text)**: Signed URL to a CSV file for `bulk_write` operation (required when operation is `bulk_write`)
  - **batch_size (Integer)**: Number of documents per `insert_many` batch for `bulk_write` (optional, default: `10000`)
- **Output parameters**:
  - **Type**: (the object type you want to map data into)
  - **Cardinality**: Single object

**For insert_one operation**, create a JSON transform for the response using this sample json:

```json
{
  "statusCode": 200,
  "operation": "insert_one",
  "inserted_id": "65a1b2c3d4e5f6789abcdef0",
  "acknowledged": false
}
```

**For bulk_write operation**, create a JSON transform for the response using this sample json:

```json
{
  "statusCode": 200,
  "operation": "bulk_write",
  "csv_count": 50000,
  "inserted_count": 50000,
  "batch_count": 5,
  "acknowledged": true
}
```

> **Note:** `acknowledged` will be `false` for individual writes due to `w=0` write concern. For bulk_write, `csv_count` is the total rows parsed from the CSV, `inserted_count` is the total documents inserted across all batches, and `batch_count` is the number of batches used.

**Example input for insert_one**:

```json
{
  "connection_string": "mongodb+srv://username:password@host/?appName=demo",
  "operation": "insert_one",
  "namespace": "mydb.mycollection",
  "document": {
    "name": "John Doe",
    "age": 30,
    "email": "john.doe@example.com"
  }
}
```

**Example input for bulk_write**:

```json
{
  "connection_string": "mongodb+srv://username:password@host/?appName=demo",
  "operation": "bulk_write",
  "namespace": "mydb.mycollection",
  "csv_url": "https://example.com/signed-url-to-data.csv"
}
```

### CSV file examples

The CSV file should have headers in the first row, and each subsequent row will be converted to a document.

**Example 1: Basic collection**

```csv
name,age,email
Alice Smith,25,alice@example.com
Bob Johnson,35,bob@example.com
Carol White,28,carol@example.com
```

This will insert 3 documents into the collection.

**Example 2: Time series collection**

For MongoDB time series collections (IoT sensors, metrics, logs, etc.), your CSV should include timestamp and measurement fields:

```csv
timestamp,sensor_id,temperature,humidity,location
2026-02-27T10:00:00Z,sensor_001,72.5,45.2,Building A
2026-02-27T10:05:00Z,sensor_001,73.1,44.8,Building A
2026-02-27T10:00:00Z,sensor_002,68.9,52.3,Building B
2026-02-27T10:05:00Z,sensor_002,69.2,51.7,Building B
```

```json
{
  "connection_string": "mongodb+srv://username:password@host/?appName=demo",
  "operation": "bulk_write",
  "namespace": "iot_data.sensor_readings",
  "csv_url": "https://example.com/signed-url-to-sensor-data.csv"
}
```

This will insert sensor readings into a time series collection. Timestamp values ending in `Z` are converted to BSON datetime objects. Ensure your MongoDB collection is created as a time series collection with appropriate timeField, metaField, and granularity settings.

## Mongo Analysis function

This function queries a MongoDB time series collection of sensor readings and returns the top 5 sensors that had the widest temperature variation in a single day, along with the date, min temperature, max temperature, and total reading count.

### Python code info

- **File**: `MongoAnalysis.py`
- **Function**: handler

### Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: MongoAnalysis.handler
- **Input parameters**:
  - **connection_string (Text)**: MongoDB connection string with embedded credentials (e.g., `mongodb+srv://username:password@host/?appName=demo`)
  - **namespace (Text)**: Database and collection in format `database.collection`
- **Output parameters**:
  - **Type**: (the object type you want to map data into)
  - **Cardinality**: Single object

Create a JSON transform for the response, using this sample json, and reference it in the function rule configuration:

```json
{
  "statusCode": 200,
  "operation": "analyze_temperature_variation",
  "namespace": "demo.sensor_readings",
  "result_count": 5,
  "top_sensors": [
    {
      "sensor_id": 578,
      "date": "2025-11-01",
      "min_temp": 22.15,
      "max_temp": 95.80,
      "temp_range": 73.65,
      "reading_count": 48
    }
  ]
}
```

**Example input**:

```json
{
  "connection_string": "mongodb+srv://username:password@host/?appName=demo",
  "namespace": "demo.sensor_readings"
}
```

The target collection should be a time series collection with documents containing at least the following fields:

| Field         | Type     | Description                          |
|---------------|----------|--------------------------------------|
| `timestamp`   | Date     | Reading timestamp                    |
| `sensor_id`   | Number   | Sensor identifier                    |
| `temperature` | Number   | Temperature reading                  |
| `voltage`     | Number   | Voltage reading                      |
| `location`    | String   | Sensor location                      |

The function uses a MongoDB aggregation pipeline to group readings by sensor and day, compute the min/max temperature per group, and return the top 5 entries sorted by the widest temperature range.

## Deployment

After a successful build, the `python-x.x.x-SNAPSHOT.zip` file is produced under `build/distributions`. This zip file includes:
- All Python source code files
- Resource files
- All dependencies specified in requirements.txt

In Pega Launchpad:

1. Select **Runtime**: Python 3.12  
2. Upload the zip file to **Code bundle**  
3. Set **Function handler** to `Calculator.handler`, `Analysis.handler`, `MongoWriter.handler`, or `MongoAnalysis.handler` depending on which example you want to use
4. Configure the input and output parameters as described above