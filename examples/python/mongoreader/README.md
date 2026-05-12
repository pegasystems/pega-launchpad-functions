# Mongo reader function

This function reads documents from a MongoDB collection and returns them as JSON, using `bson.json_util` to safely serialize BSON types (e.g., `ObjectId`, `Date`).

## Python code info

- **File**: `MongoReader.py`
- **Function**: `handler`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `MongoReader.handler`
- **Input parameters**:
  - **connection_string (Text)**: MongoDB connection string with embedded credentials (e.g., `mongodb+srv://username:password@host/?appName=demo`)
  - **namespace (Text)**: Database and collection in format `database.collection`
  - **filter (Object)**: Optional MongoDB query filter
  - **limit (Integer)**: Optional maximum number of documents to return
- **Output parameters**:
  - **Type**: (the object type you want to map data into)
  - **Cardinality**: Single object

## Build

From the repository root:

```
gradlew :examples:python:mongoreader:build
```

The zip artifact is produced under `build/distributions/mongoreader-x.y.z-SNAPSHOT.zip`.
