# Google Cloud Platform (GCP) Java SDK integration example

This java code can be imported into a Function rule in your application for simple GCP service integration.

<!-- TOC -->
* [Google Cloud Platform (GCP) Java SDK integration example](#google-cloud-platform-gcp-java-sdk-integration-example)
  * [Pre-requisities](#pre-requisities)
  * [Authentication information](#authentication-information)
  * [Class: GCPHelper](#class-gcphelper)
    * [Method: createBucket()](#method-createbucket)
    * [Method: createObject()](#method-createobject)
    * [Method: getObject()](#method-getobject)
  * [Running unit tests](#running-unit-tests)
<!-- TOC -->

## Pre-requisities

These examples require the creation and use of [service account keys](https://cloud.google.com/iam/docs/service-account-creds#key-types) to 
authenticate against your gcp account through the GCP authentication SDKs.

The service accounts must have the necessary privileges for the APIs being invoked.

Use of service accounts for authentication can be considered risky: if needed, change the helper code to use a different
form of authentication based on your needs.

## Authentication information

All functions require these inputs to authenticate:

- base64EncodedJsonCredentials (String): Base64 encoded form of your service account "key", which is a json file you download from GCP

## Class: GCPHelper

This example java class shows how to integrate with [GCP Cloud Storage](https://cloud.google.com/storage/docs/reference/libraries).

### Method: createBucket()

**Function handler: com.pega.launchpad.gcp.GCPHelper::createBucket**

This will create a new bucket in your GCP account.

Inputs:

- bucketName (String): AWS compliant name for your new bucket

Sample json output (use json transform to map back to your object data model):

```
{
  "generatedId": "com-pega-launchpad-gcp-bucket1746730380953",
  "name": "com-pega-launchpad-gcp-bucket1746730380953",
  "owner": {
    "projectRole": {
      "constant": "OWNERS"
    },
    "projectId": "776753144583",
    "type": "PROJECT",
    "value": "owners-776753144583"
  },
  "selfLink": "https://www.googleapis.com/storage/v1/b/com-pega-launchpad-gcp-bucket1746730380953",
  "etag": "CAE=",
  "createTime": {
    "dateTime": {
      "date": {
        "year": 2025,
        "month": 5,
        "day": 8
      },
      "time": {
        "hour": 18,
        "minute": 53,
        "second": 3,
        "nano": 65000000
      }
    },
    "offset": {
      "totalSeconds": 0
    }
  },
  "updateTime": {
    "dateTime": {
      "date": {
        "year": 2025,
        "month": 5,
        "day": 8
      },
      "time": {
        "hour": 18,
        "minute": 53,
        "second": 3,
        "nano": 65000000
      }
    },
    "offset": {
      "totalSeconds": 0
    }
  },
  "metageneration": 1,
  "acl": [
    {
      "entity": {
        "projectRole": {
          "constant": "OWNERS"
        },
        "projectId": "776753144583",
        "type": "PROJECT",
        "value": "owners-776753144583"
      },
      "role": {
        "constant": "OWNER"
      },
      "id": "com-pega-launchpad-gcp-bucket1746730380953/project-owners-776753144583",
      "etag": "CAE="
    },
    {
      "entity": {
        "projectRole": {
          "constant": "EDITORS"
        },
        "projectId": "776753144583",
        "type": "PROJECT",
        "value": "editors-776753144583"
      },
      "role": {
        "constant": "OWNER"
      },
      "id": "com-pega-launchpad-gcp-bucket1746730380953/project-editors-776753144583",
      "etag": "CAE="
    },
    {
      "entity": {
        "projectRole": {
          "constant": "VIEWERS"
        },
        "projectId": "776753144583",
        "type": "PROJECT",
        "value": "viewers-776753144583"
      },
      "role": {
        "constant": "READER"
      },
      "id": "com-pega-launchpad-gcp-bucket1746730380953/project-viewers-776753144583",
      "etag": "CAE="
    }
  ],
  "defaultAcl": [
    {
      "entity": {
        "projectRole": {
          "constant": "OWNERS"
        },
        "projectId": "776753144583",
        "type": "PROJECT",
        "value": "owners-776753144583"
      },
      "role": {
        "constant": "OWNER"
      },
      "etag": "CAE="
    },
    {
      "entity": {
        "projectRole": {
          "constant": "EDITORS"
        },
        "projectId": "776753144583",
        "type": "PROJECT",
        "value": "editors-776753144583"
      },
      "role": {
        "constant": "OWNER"
      },
      "etag": "CAE="
    },
    {
      "entity": {
        "projectRole": {
          "constant": "VIEWERS"
        },
        "projectId": "776753144583",
        "type": "PROJECT",
        "value": "viewers-776753144583"
      },
      "role": {
        "constant": "READER"
      },
      "etag": "CAE="
    }
  ],
  "location": "US",
  "rpo": {
    "constant": "DEFAULT"
  },
  "storageClass": {
    "constant": "STANDARD"
  },
  "iamConfiguration": {
    "isUniformBucketLevelAccessEnabled": false,
    "publicAccessPrevention": "INHERITED"
  },
  "locationType": "multi-region",
  "softDeletePolicy": {
    "retentionDuration": {
      "seconds": 604800,
      "nanos": 0
    },
    "effectiveTime": {
      "dateTime": {
        "date": {
          "year": 2025,
          "month": 5,
          "day": 8
        },
        "time": {
          "hour": 18,
          "minute": 53,
          "second": 3,
          "nano": 65000000
        }
      },
      "offset": {
        "totalSeconds": 0
      }
    }
  }
}
```

### Method: createObject()

This method will put an object into a specific bucket.

**Function handler: com.pega.launchpad.gcp.GCPHelper::createObject**

Inputs:

1. bucketName (String): Name of the bucket
2. objectName (String): Name for the object
3. objectBase64 (String): base64 encoded content of the object

Sample json output (use json transform to map back to your object data model):

```
{
  "blobId": {
    "bucket": "com-pega-launchpad-gcp-bucket1746730511893",
    "name": "object1746730511893",
    "generation": 1746730514898019
  },
  "generatedId": "com-pega-launchpad-gcp-bucket1746730511893/object1746730511893/1746730514898019",
  "selfLink": "https://www.googleapis.com/storage/v1/b/com-pega-launchpad-gcp-bucket1746730511893/o/object1746730511893",
  "size": 14,
  "etag": "COO4757GlI0DEAE=",
  "md5": "VLDFjHzp8qi1UTURAu4JOA==",
  "crc32c": "fPxmpw==",
  "mediaLink": "https://storage.googleapis.com/download/storage/v1/b/com-pega-launchpad-gcp-bucket1746730511893/o/object1746730511893?generation=1746730514898019&alt=media",
  "metageneration": 1,
  "updateTime": {
    "dateTime": {
      "date": {
        "year": 2025,
        "month": 5,
        "day": 8
      },
      "time": {
        "hour": 18,
        "minute": 55,
        "second": 14,
        "nano": 914000000
      }
    },
    "offset": {
      "totalSeconds": 0
    }
  },
  "createTime": {
    "dateTime": {
      "date": {
        "year": 2025,
        "month": 5,
        "day": 8
      },
      "time": {
        "hour": 18,
        "minute": 55,
        "second": 14,
        "nano": 914000000
      }
    },
    "offset": {
      "totalSeconds": 0
    }
  },
  "contentType": "application/octet-stream",
  "storageClass": {
    "constant": "STANDARD"
  },
  "timeStorageClassUpdated": {
    "dateTime": {
      "date": {
        "year": 2025,
        "month": 5,
        "day": 8
      },
      "time": {
        "hour": 18,
        "minute": 55,
        "second": 14,
        "nano": 914000000
      }
    },
    "offset": {
      "totalSeconds": 0
    }
  },
  "isDirectory": false
}
```

### Method: getObject()

This method will get the base64 encoded content of an object from a specific bucket.

**Function handler: com.pega.launchpad.gcp.GCPHelper::getObject**

Inputs:

1. bucketName (String): Name of the bucket
2. objectKey (String): Name of the object

Output:

- String: The base64 encoded object content

## Running unit tests

To successfully run the unit tests locally, you'll need to set an environmental variable:

1. base64EncodedJsonCredentials: the base64-encoded json of your gcp service account key
