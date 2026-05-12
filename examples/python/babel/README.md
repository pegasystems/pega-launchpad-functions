# Format currency function

This function formats a numeric amount as a localized currency string using the [Babel](https://github.com/python-babel/babel) library. It supports multiple currencies and locales for proper currency symbol and number formatting.

## Python code info

- **File**: `Babel.py`
- **Function**: `format_currency`

## Function rule configuration

- **Runtime**: Python 3.12
- **Function handler**: `Babel.format_currency`
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

## Build

From the repository root:

```
gradlew :examples:python:babel:build
```

The zip artifact is produced under `build/distributions/babel-x.y.z-SNAPSHOT.zip`.
