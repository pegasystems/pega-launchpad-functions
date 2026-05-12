# Calculator function

This function performs basic mathematical operations (addition, subtraction, multiplication, and division).

## Python code info

- **File**: `Calculator.py`
- **Function**: `handler`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `Calculator.handler`
- **Input parameters**:
  - **num1 (Integer)**: number 1
  - **num2 (Integer)**: number 2
- **Output parameters**:
  - **Type**: (the object type you want to map data into)
  - **Cardinality**: Single object

Create a JSON transform for the response, using this sample json, and reference it in the function rule configuration:

```json
{
  "addition": 8,
  "subtraction": -2,
  "multiplication": 15,
  "division": 0.6
}
```

## Build

From the repository root:

```
gradlew :examples:python:calculator:build
```

The zip artifact is produced under `build/distributions/calculator-x.y.z-SNAPSHOT.zip`.
