# Python Functions

This directory contains a collection of Python function examples for Pega Launchpad. Each function lives in its **own subproject** with its own `requirements.txt`, `README.md`, and Gradle build script, so building one function only pulls its own dependencies and produces a small, focused deployment zip.

## Available functions

| Subproject | Description |
|------------|-------------|
| [calculator](./calculator)       | Basic math operations (no third-party deps) |
| [analysis](./analysis)           | CSV voltage data min/max analysis (`requests`) |
| [babel](./babel)                 | Currency formatting via `babel` |
| [emailtojson](./emailtojson)     | Parse `.eml` / `.msg` files to JSON (`extract-msg`) |
| [mongoreader](./mongoreader)     | Read documents from MongoDB (`pymongo[srv]`) |
| [mongowriter](./mongowriter)     | Insert / bulk-write documents to MongoDB (`pymongo[srv]`, `requests`, `certifi`) |
| [mongoanalysis](./mongoanalysis) | Aggregation analysis on a MongoDB time-series collection (`pymongo[srv]`) |

See each subproject's README for handler configuration, input/output schemas, and example payloads.

## Build

From the repository root, build everything:

```
gradlew build
```

This will build every Python subproject and produce one zip per function under `examples/python/<name>/build/distributions/<name>-x.y.z-SNAPSHOT.zip`.

To build a single function (recommended when you only need one):

```
gradlew :examples:python:calculator:build
gradlew :examples:python:emailtojson:build
```

## Layout of a subproject

```
examples/python/<name>/
├── build.gradle.kts        # Minimal: applies the python-zip convention from the parent
├── requirements.txt        # pip-installable dependencies (may be empty)
├── README.md               # Function description & handler configuration
├── resources/              # (optional) Static files bundled into the zip
└── src/                    # Python source files
```

The shared zip-building convention is defined once in [examples/python/build.gradle.kts](./build.gradle.kts) and applied to every subproject. To add a new Python function, create a new subdirectory with the layout above and add it to the root [settings.gradle.kts](../../settings.gradle.kts).

## Deployment

After a successful build, upload the per-function zip to Pega Launchpad:

1. Select **Runtime**: Python 3.12
2. Upload the zip file (e.g. `calculator-x.y.z-SNAPSHOT.zip`) to **Code bundle**
3. Set **Function handler** to the value documented in that function's README (e.g. `Calculator.handler`, `Analysis.handler`, `Babel.format_currency`, `EmailToJson.handler`, `MongoReader.handler`, `MongoWriter.handler`, or `MongoAnalysis.handler`)
4. Configure the input and output parameters as described in the function's README
