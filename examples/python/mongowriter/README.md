# Mongo writer function

This function provides MongoDB operations for inserting single documents or performing bulk write operations using the pymongo driver. It uses a `w=0` (unacknowledged) write concern for maximum write throughput.

## Python code info

- **File**: `MongoWriter.py`
- **Function**: `handler`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `MongoWriter.handler`
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

## CSV file examples

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

## Build

From the repository root:

```
gradlew :examples:python:mongowriter:build
```

The zip artifact is produced under `build/distributions/mongowriter-x.y.z-SNAPSHOT.zip`.
