# Amazon Web Services Java SDK integration example

This java code can be imported into a Function rule in your application for simple AWS service integration.

<!-- TOC -->
* [Amazon Web Services Java SDK integration example](#amazon-web-services-java-sdk-integration-example)
  * [Pre-requisities](#pre-requisities)
  * [Authentication information](#authentication-information)
  * [Class: S3Helper](#class-s3helper)
    * [Method: createBucket()](#method-createbucket)
    * [Method: listBuckets()](#method-listbuckets)
    * [Method: putObject()](#method-putobject)
    * [Method: getObject()](#method-getobject)
  * [Class: ComprehendHelper](#class-comprehendhelper)
    * [Method: detectKeyPhrases()](#method-detectkeyphrases)
  * [Class: TranslateHelper](#class-translatehelper)
    * [Method: translateText()](#method-translatetext)
  * [Running unit tests](#running-unit-tests)
<!-- TOC -->

## Pre-requisities

You will need to set up an [access key](https://docs.aws.amazon.com/console/iam/self-accesskeys) in your AWS account.

## Authentication information

All functions require these inputs to authenticate:

- accessKeyId (String): Your Access Key ID
- secretAccessKey (String): The secret access key corresponding to your access key

## Class: S3Helper

This example java class shows how to integrate with Amazon Web Service's [S3](https://aws.amazon.com/pm/serv-s3/?trk=7b66aa9c-13c9-40da-8b37-5a2ff352c708&sc_channel=ps&ef_id=Cj0KCQjw5ubABhDIARIsAHMighY34aI369Hko0T3qVkabTchJNKpI_gdrckbX6eU1J-87qzVi-sq1vsaAvWSEALw_wcB:G:s&s_kwcid=AL!4422!3!747067299004!e!!g!!aws%20s3!22451713848!181225714747&gad_campaignid=22451713848&gbraid=0AAAAADjHtp__NuOmbnLidrcgusKbD4yqb&gclid=Cj0KCQjw5ubABhDIARIsAHMighY34aI369Hko0T3qVkabTchJNKpI_gdrckbX6eU1J-87qzVi-sq1vsaAvWSEALw_wcB)

### Method: createBucket()

This will create a new bucket in S3.

Inputs:

- bucketName (String): AWS compliant name for your new bucket

Sample json output (use json transform to map back to your object data model):

```
{
  "Location": "/newbucket1746560093607"
}
```

### Method: listBuckets()

This example function will return a list of s3 buckets in your account

Sample json output (use json transform to map back to your object data model):

```
{
  "Buckets": [
    {
      "Name": "..",
      "CreationDate": ".."
    }
  ],
  "Owner": {
    "DisplayName": "..",
    "ID": ".."
  }
}
```

### Method: putObject()

This method will put an object into a specific bucket.

Inputs:

1. bucketName (String): Name of the bucket
2. objectKey (String): Key for the object in s3
3. objectBase64 (String): base64 encoded content of the object

Sample json output (use json transform to map back to your object data model):

```
{
  "ETag": "50b0c58c7ce9f2a8b551351102ee0930",
  "ServerSideEncryption": "AES256"
}
```

### Method: getObject()

This method will get the base64 encoded content of an object from a specific bucket.

Inputs:

1. bucketName (String): Name of the bucket
2. objectKey (String): Key for the object in s3

Output:

- String: The base64 encoded object content

## Class: ComprehendHelper

This example shows how to integrate with Amazon's [Comprehend](https://aws.amazon.com/comprehend/) service
for natural language processing.

### Method: detectKeyPhrases()

This method, given some text, will detect the key noun phrases found in that text using the [detectKeyPhrases](https://docs.aws.amazon.com/comprehend/latest/APIReference/API_DetectKeyPhrases.html) API.

Inputs:

1. text (String): The phrase to process
2. languageCode (String): Optional short language code, defaults to "en"
3. minimumScore (String): Optional minimum score (0 to 0.999) for key phrase to be included in response

Sample json output (use json transform to map back to your object data model):

```
[
  {
    "score": "0.999525",
    "keyPhrase": "our platform"
  },
  {
    "score": "0.99966145",
    "keyPhrase": "their most pressing challenges"
  },
  {
    "score": "0.99932826",
    "keyPhrase": "1983"
  },
  {
    "score": "0.99923444",
    "keyPhrase": "enterprises"
  },
  {
    "score": "0.99958104",
    "keyPhrase": "tomorrow"
  }
]
```

## Class: TranslateHelper

This class shows how to integrate with AWS Translate service.

### Method: translateText()

This method will translate a line of text from a source language to a target language.

Inputs:

1. text (String): The text to translate
2. sourceLanguageCode (String): Source language (like "en")
3. targetLanguageCode (String): Target language (like "es")

Output:

- String: The translated text

## Running unit tests

To successfully run the unit tests locally, you'll need to set two environmental variables:

1. accessKeyId: your aws access key
2. secretAccessKey: your aws access key secret
