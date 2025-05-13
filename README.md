# Overview

[Pega Launchpad](https://launchpad.io) is the platform for SaaS development, hosting and operations that empowers you to get workflow-centric B2B applications to market and revenue quickly, freeing up your resources to focus on innovation, customer success and business growth.

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

| Service                                                                                                                                                                    | Name                                          | Description                                  | Latest jar/zip file                                                                                                         |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Amazon_Web_Services_Logo.svg/512px-Amazon_Web_Services_Logo.svg.png" width=51 height=31 />             | [aws](examples/aws)                           | AWS helpers (s3, comprehend, translate, kms) | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/aws-0.2.4-SNAPSHOT.jar)              |
|                                                                                                                                                                            | [base64](examples/base64)                     | Encoding/decoding base64 strings             | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/base64-0.2.4-SNAPSHOT.jar)           |
|                                                                                                                                                                            | [businesscalendar](examples/businesscalendar) | Nodejs business calendar example             | [zip](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/businesscalendar-0.2.4-SNAPSHOT.zip) |
| <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Docusign_Full_Color.svg/768px-Docusign_Full_Color.svg.png?20240411204040" width=102 height=20 />       | [docusign](examples/docusign)                 | Docusign e-signature example                 | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/docusign-0.2.4-SNAPSHOT.jar)         | 
|                                                                                                                                                                            | [email](examples/email)                       | Email utilities                              | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/email-0.2.4-SNAPSHOT.jar)            | 
| <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Google_Cloud_logo.svg/768px-Google_Cloud_logo.svg.png" height=16 width=100 />                          | [gcp](examples/gcp)                           | Google Cloud Platform helpers (storage sdk)  | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/gcp-0.2.4-SNAPSHOT.jar)              |
| <img src="https://upload.wikimedia.org/wikipedia/commons/d/dc/Google_Maps_Logo.svg" height=20 width=100 />                                                                 | [geocoding](examples/geocoding)               | Geocoding utilities                          | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/geocoding-0.2.4-SNAPSHOT.jar)        |
|                                                                                                                                                                            | [net](examples/net)                           | REST and HTTP utilities                      | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/net-0.2.4-SNAPSHOT.jar)              | 
| <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Node.js_logo.svg/330px-Node.js_logo.svg.png" width=32 height=20 />                                     | [nodejs](examples/nodejs)                     | Nodejs function example                      | [zip](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/nodejs-0.2.4-SNAPSHOT.zip)           |
| <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Oracle_Corporation_logo.svg/48px-Oracle_Corporation_logo.svg.png?20220125115241" width=32 height=20 /> | [oracle](examples/oracle)                     | Oracle Cloud Infrastructure (object storage) | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/oracle-0.2.4-SNAPSHOT.jar)           |
|                                                                                                                                                                            | [parser](examples/parser)                     | CSV and JSON parsing                         | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/parser-0.2.4-SNAPSHOT.jar)           | 
|                                                                                                                                                                            | [pdf](examples/pdf)                           | PDF form filler                              | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/pdf-0.2.4-SNAPSHOT.jar)              |
| <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/219px-Python-logo-notext.svg.png" width=22 height=24 />                         | [python](examples/python)                     | Python function example                      | [zip](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/python-0.2.4-SNAPSHOT.zip)           |
|                                                                                                                                                                            | [text](examples/text)                         | String utilities                             | [jar](https://github.com/pegasystems/pega-launchpad-functions/releases/download/v0.2.4/text-0.2.4-SNAPSHOT.jar)             | 

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

