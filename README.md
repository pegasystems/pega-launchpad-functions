# Overview

[Pega Launchpad](https://launchpad.io/) is the platform for SaaS development, hosting and operations that empowers you to get workflow-centric B2B applications to market and revenue quickly, freeing up your resources to focus on innovation, customer success and business growth.

This repository contains some sample code and configuration to learn how to create your own [functions](https://docs.pega.com/bundle/launchpad/page/platform/launchpad/creating-custom-functions.html) in Pega Launchpad. Use as examples or starter code for your own functions as needed.

# Table of Contents

<!-- TOC -->
* [Overview](#overview)
* [Table of Contents](#table-of-contents)
* [References](#references)
* [Modules](#modules)
* [Obtaining a JAR to upload into a Function rule](#obtaining-a-jar-to-upload-into-a-function-rule)
<!-- TOC -->

# References

- Pega Launchpad [overview](https://launchpad.io)
- Creating [Function](https://docs.pega.com/bundle/launchpad/page/platform/launchpad/creating-custom-functions.html) rules

# Modules

| Name                    | Description                      | Latest JAR file                                                                                                      |
|-------------------------|----------------------------------|----------------------------------------------------------------------------------------------------------------------|
| [base64](base64/)       | Encoding/decoding base64 strings | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.1.9/base64-0.1.9-SNAPSHOT.jar)    |
| [email](email/)         | email utilities                  | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.1.9/email-0.1.9-SNAPSHOT.jar)     | 
| [geocoding](geocoding/) | Geocoding utilities              | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.1.9/geocoding-0.1.9-SNAPSHOT.jar) |
| [net](net/)             | REST and HTTP utilities          | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.1.9/net-0.1.9-SNAPSHOT.jar)       | 
| [parser](parser/)       | CSV and JSON parsing             | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.1.9/parser-0.1.9-SNAPSHOT.jar)    | 
| [pdf](pdf/)             | PDF form filler                  | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.1.9/pdf-0.1.9-SNAPSHOT.jar)       | 
| [text](text/)           | string utilities                 | [jar](https://github.com/pegasystems/pega-launchpad-functions/download/v0.1.9/text-0.1.9-SNAPSHOT.jar)               | 

# Obtaining a JAR to upload into a Function rule

You can download the latest JAR releases for each module from the [project release page](https://github.com/pegasystems/pega-launchpad-functions/releases), or you can generate the necessary JAR file (including dependencies) for uploading into a Pega Launchpad Function rule by running this command in your local workspace:

```gradlew build```

This will create/update the ```module-vX.Y.Z-SNAPSHOT.jar``` files in the various modules, under their ```build/libs``` directory.

# Setting up IntellJ workspace for this project

1. Clone this git project to your local filesystems: ```git clone https://github.com/pegasystems/pega-launchpad-functions.git```
2. In IntelliJ, open this project from your local folder
3. Configure Settings -> Build, Execution, Deployment -> Build Tools -> Gradle to use a jdk 11, like semeru-11-*. Add/download from there if needed
4. Configure Project Settings -> Project -> to use semeru-11-*, language level 11

To run gradle build scripts from command line, make sure you are using at least JDK 11, but nothing higher than that. Check your path and JAVA_HOME settings, download new jdk if necessary.

