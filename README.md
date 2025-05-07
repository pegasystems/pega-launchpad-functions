# Overview

[Pega Launchpad](https://launchpad.io/) is the platform for SaaS development, hosting and operations that empowers you to get workflow-centric B2B applications to market and revenue quickly, freeing up your resources to focus on innovation, customer success and business growth.

This repository contains some sample code and configuration to learn how to create your own [functions](https://docs.pega.com/bundle/launchpad/page/platform/launchpad/creating-custom-functions.html) in Pega Launchpad. Use as examples or starter code for your own functions as needed.

# Table of Contents

<!-- TOC -->
* [Overview](#overview)
* [Table of Contents](#table-of-contents)
* [References](#references)
* [Java SDK](#java-sdk)
* [Example modules](#example-modules)
  * [Example modules: Obtaining a JAR or ZIP to import into a Function rule](#example-modules-obtaining-a-jar-or-zip-to-import-into-a-function-rule)
* [Setting up IntellJ workspace for this project](#setting-up-intellj-workspace-for-this-project)
<!-- TOC -->

# References

- Pega Launchpad [overview](https://launchpad.io)
- Creating [Function](https://docs.pega.com/bundle/launchpad/page/platform/launchpad/creating-custom-functions.html) rules

# Java SDK

This repository also includes a java software development kit (SDK) to assist in the development of Launchpad Functions using the following runtime languages:

- [java](sdk/java)

# Example modules

| Name                             | Description                      | Latest JAR file                                                                                                      |
|----------------------------------|----------------------------------|----------------------------------------------------------------------------------------------------------------------|
| [aws](examples/aws/)             | AWS helpers (s3, comprehend)     | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/aws-0.2.3-SNAPSHOT.jar)    |
| [base64](examples/base64/)       | Encoding/decoding base64 strings | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/base64-0.2.3-SNAPSHOT.jar)    |
| [docusign](examples/docusign/)   | Docusign e-signature example     | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/docusign-0.2.3-SNAPSHOT.jar)     | 
| [email](examples/email/)         | Email utilities                  | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/email-0.2.3-SNAPSHOT.jar)     | 
| [geocoding](examples/geocoding/) | Geocoding utilities              | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/geocoding-0.2.3-SNAPSHOT.jar) |
| [net](examples/net/)             | REST and HTTP utilities          | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/net-0.2.3-SNAPSHOT.jar)       | 
| [nodejs](examples/nodejs/)       | Nodejs function example          | [zip](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/nodejs-0.2.3-SNAPSHOT.jar) |
| [parser](examples/parser/)       | CSV and JSON parsing             | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/parser-0.2.3-SNAPSHOT.jar)    | 
| [pdf](examples/pdf/)             | PDF form filler                  | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/pdf-0.2.3-SNAPSHOT.jar)       |
| [python](examples/python/)       | Python function example          | [zip](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/python-0.2.3-SNAPSHOT.jar)  |
| [text](examples/text/)           | String utilities                 | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.3/text-0.2.3-SNAPSHOT.jar)               | 

## Example modules: Obtaining a JAR or ZIP to import into a Function rule

You can download the latest JAR releases for each module from the [project release page](https://github.com/pegasystems/pega-launchpad-functions/releases), or you can generate the necessary JAR file (including dependencies) for uploading into a Pega Launchpad Function rule by running this command in your local workspace:

```gradlew build```

- For java examples, this will create/update the ```module-vX.Y.Z-SNAPSHOT.jar``` files in the various example modules, under their ```build/libs``` directory.
- For python examples, this will create/update the ```nodejs-vX.Y.Z-SNAPSHOT.zip``` file in the ```examples/nodejs/build/distributions```
- For nodejs examples, this will create/update the ```python-vX.Y.Z-SNAPSHOT.zip``` file in the ```example/python/build/distributions``` directory

# Setting up IntellJ workspace for this project

1. Clone this git project to your local filesystems: ```git clone https://github.com/pegasystems/pega-launchpad-functions.git```
2. In IntelliJ, open this project from your local folder
3. Configure Settings -> Build, Execution, Deployment -> Build Tools -> Gradle to use a jdk 11, like semeru-11-*. Add/download from there if needed
4. Configure Project Settings -> Project -> to use semeru-11-*, language level 11

To run gradle build scripts from command line, make sure you are using at least JDK 11, but nothing higher than that. Check your path and JAVA_HOME settings, download new jdk if necessary.

