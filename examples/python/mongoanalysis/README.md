# Mongo Analysis function

This function queries a MongoDB time series collection of sensor readings and returns the top 5 sensors that had the widest temperature variation in a single day, along with the date, min temperature, max temperature, and total reading count.

## Python code info

- **File**: `MongoAnalysis.py`
- **Function**: `handler`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `MongoAnalysis.handler`
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

## Build

From the repository root:

```
gradlew :examples:python:mongoanalysis:build
```

The zip artifact is produced under `build/distributions/mongoanalysis-x.y.z-SNAPSHOT.zip`.
