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
- Dependencies: [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/), [Google Gson](https://github.com/google/gson), [Apache PDFBox](https://pdfbox.apache.org/)

# Modules

1. Base64 functions: [base64](base64/README.md)
2. Parsing functions: [parser](parser/README.md)
3. PDF functions: [pdf](pdf/README.md)
4. Text functions: [text](text/README.md)

# Obtaining a JAR to upload into a Function rule

You can download the latest JAR releases for each module from the [project release page](https://github.com/miratim/PegaLPSTTools/releases), or you can generate the necessary JAR file (including dependencies) for uploading into a Pega Launchpad Function rule, run this command:

```gradlew build```

This will create/update the ```pega-launchpad-functions-*.jar``` files in the various modules, under their ```build/libs``` directory.

