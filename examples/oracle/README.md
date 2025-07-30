# Oracle Cloud Infrastructure (OCI) Java SDK integration example

This java code can be imported into a Function rule in your application for simple Oracle Cloud Infrastructure integration.

<!-- TOC -->
* [Oracle Cloud Infrastructure (OCI) Java SDK integration example](#oracle-cloud-infrastructure-oci-java-sdk-integration-example)
  * [Pre-requisities](#pre-requisities)
  * [Authentication information](#authentication-information)
  * [Class: ObjectStorageSyncExample](#class-objectstoragesyncexample)
    * [Method: createBucket()](#method-createbucket)
    * [Method: listBuckets()](#method-listbuckets)
    * [Method: putObject()](#method-putobject)
    * [Method: getObject()](#method-getobject)
<!-- TOC -->

## Pre-requisities

You will need to set up your account, users, and keys as described [here](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/apisigningkey.htm#Required_Keys_and_OCIDs).

Note that the generated elements on your config file, as well as the content of the private key (PEM File), will need to be passed into each method in this example as individual values, so 
please record these elements for use when configuring your Function rule:

user - the OCID of the user for whom the key pair is being added.
fingerprint - the fingerprint of the key that was just added.
tenancy - your tenancy's OCID.
region - the currently selected region in the Console.

You will have generated a public RSA key in PEM format (minimum 2048 bits). The PEM format looks something like this:

```
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoTFqF...
...
-----END PUBLIC KEY——
```


## Authentication information

All functions require these inputs to authenticate:

- userId (String): the OCID of the user for whom the key pair is being added in your OCI admin configuration.
- fingerprint (String): the fingerprint of the key that was just added for that user.
- tenantId (String): the 'tenancy' value for your OCI configuration
- region (String): the 'region' value for your OCI configuration
- profile (String): the profile for your account (usually 'DEFAULT')
- privateKeyBase64 (String): The base64 encoded value of your private key PEM file

## Class: ObjectStorageSyncExample

This example java class shows how to integrate with Oracle Cloud Infrastructure (OCI) [Cloud Storage service](https://docs.oracle.com/en-us/iaas/Content/Object/home.htm).

### Method: createBucket()

This will create a new bucket in your cloud storage.

**Function handler: com.pega.launchpad.oracle.ObjectStorageSyncExample::createBucket**

Inputs:

- bucketName (String): OCI compliant name for your new bucket

Sample json output (use json transform to map back to your object data model):

```
{
  "opcRequestId": "iad-1:OCqp7HmhbH5YrkoKKaP8uHL6Zao0_aOlGEL_6gd12342nswAmafIbt",
  "eTag": "d3eb9f04-7cbc-43e2-83f8-bc5781a77669",
  "location": "https://objectstorage.us-ashburn-1.oraclecloud.com/n/idt12343q/b/bucket-174123494",
  "bucket": {
    "namespace": "idt12343q",
    "name": "bucket-174123494",
    "compartmentId": "ocid1.tenancy.oc1..aaaaaaaaaniomwaloc3ym5z412342rwo4iiii6pfbvdq",
    "metadata": {},
    "createdBy": "ocid1.user.oc1..aaaaaaaaxv4emcognrn4yr3eo1234gyc675ftj7qa",
    "timeCreated": "May 10, 2025, 7:37:08 PM",
    "etag": "d3eb9f04-7cbc-43e2-1234-bc5781a77669",
    "publicAccessType": "NoPublicAccess",
    "storageTier": "Standard",
    "objectEventsEnabled": false,
    "freeformTags": {},
    "definedTags": {
      "Oracle-Tags": {
        "CreatedBy": "default/noreply@pega.com",
        "CreatedOn": "2025-05-10T23:37:08.182Z"
      }
    },
    "replicationEnabled": false,
    "isReadOnly": false,
    "id": "ocid1.bucket.oc1.iad.aaaaaaa123lfyzz3vfxnwme3rez2axbxwrxa7b3gvnzugrof3ddoq",
    "versioning": "Disabled",
    "__explicitlySet__": [
      "metadata",
      "versioning",
      "objectEventsEnabled",
      "autoTiering",
      "compartmentId",
      "replicationEnabled",
      "storageTier",
      "approximateSize",
      "publicAccessType",
      "isReadOnly",
      "createdBy",
      "namespace",
      "name",
      "timeCreated",
      "etag",
      "id",
      "kmsKeyId",
      "definedTags",
      "freeformTags",
      "approximateCount"
    ]
  },
  "__httpStatusCode__": 200,
  "headers": {
    "Content-Type": [
      "application/json"
    ],
    "location": [
      "https://objectstorage.us-ashburn-1.oraclecloud.com/n/idtkj0g1k43q/b/bucket-1712346594"
    ],
    "etag": [
      "d3eb9f04-7cbc-43e2-83f8-bc5712349"
    ],
    "Content-Length": [
      "914"
    ],
    "date": [
      "Sat, 10 May 2025 23:37:08 GMT"
    ],
    "opc-request-id": [
      "iad-1:OCqp7HmhbH5YrkoKKaP8uHL6Zao0_aOlGEL_61234GUZPYb8Y2nswAmafIbt"
    ],
    "x-api-id": [
      "native"
    ],
    "x-content-type-options": [
      "nosniff"
    ],
    "strict-transport-security": [
      "max-age=31536000; includeSubDomains"
    ],
    "access-control-allow-origin": [
      "*"
    ],
    "access-control-allow-methods": [
      "POST,PUT,GET,HEAD,DELETE,OPTIONS"
    ],
    "access-control-allow-credentials": [
      "true"
    ],
    "access-control-expose-headers": [
      "access-control-allow-credentials,access-control-allow-methods,access-control-allow-origin,content-length,content-type,date,etag,location,opc-client-info,opc-request-id,strict-transport-security,x-api-id,x-content-type-options"
    ]
  }
}
```

### Method: listBuckets()

This will list all the buckets in your profile.

**Function handler: com.pega.launchpad.oracle.ObjectStorageSyncExample::listBuckets**

Sample json output (use json transform to map back to your object data model):

```
[
  {
    "namespace": "jghjfhg",
    "name": "bucket-20250510-1850",
    "compartmentId": "ocid1.tenancy.oc1..1234",
    "createdBy": "ocid1.user.oc1..1234",
    "timeCreated": "May 10, 2025, 6:50:55 PM",
    "etag": "b0665b59-cdbe-4dbe-b64b-1234",
    "__explicitlySet__": [
      "compartmentId",
      "createdBy",
      "namespace",
      "name",
      "timeCreated",
      "etag",
      "definedTags",
      "freeformTags"
    ]
  },
  {
    "namespace": "idtkj0g1234",
    "name": "bucket-20250510-1851",
    "compartmentId": "ocid1.tenancy.oc1..aaaaaaaaani1234",
    "createdBy": "ocid1.user.oc1..aaaaaaaaxv1234",
    "timeCreated": "May 10, 2025, 6:51:08 PM",
    "etag": "0c40e507-cfac-4c24-9ddf-1234",
    "__explicitlySet__": [
      "compartmentId",
      "createdBy",
      "namespace",
      "name",
      "timeCreated",
      "etag",
      "definedTags",
      "freeformTags"
    ]
  }
]
```

### Method: putObject()

This method will put an object into a specific bucket.

**Function handler: com.pega.launchpad.oracle.ObjectStorageSyncExample::putObject**

Inputs:

1. bucketName (String): Name of the bucket
2. objectName (String): Key for the object in the bucket
3. objectBase64 (String): base64 encoded content of the object

Sample json output (use json transform to map back to your object data model):

```
{
  "opcRequestId": "iad-1:M_YcaZ3o5LmidKts183g0vrqjXl-SSd6ylL1234Vqx1PengW3XaKXMuk5ORjpWSLu",
  "opcContentMd5": "VLDFjH1234RAu4JOA==",
  "eTag": "b7a2d571-e429-1234-8aa2-e7a70e6e94ca",
  "lastModified": "May 10, 2025, 7:27:07 PM",
  "versionId": "d7f4efab-7aa1-4074-1234-2c9222375bfd",
  "__httpStatusCode__": 200,
  "headers": {
    "etag": [
      "b7a2d571-e429-1234-8aa2-e7a70e6e94ca"
    ],
    "last-modified": [
      "Sat, 10 May 2025 23:27:07 GMT"
    ],
    "opc-content-md5": [
      "VLDFjHzp8qi1UTURAu4JOA=="
    ],
    "version-id": [
      "d7f4efab-7aa1-1234-bc6a-2c9222375bfd"
    ],
    "Content-Length": [
      "0"
    ],
    "date": [
      "Sat, 10 May 2025 23:27:07 GMT"
    ],
    "opc-request-id": [
      "iad-1:M_YcaZ3o5LmidKts11234rqjXl-SSd6ylL00cVqx1PengW3XaKXMuk5ORjpWSLu"
    ],
    "x-api-id": [
      "native"
    ],
    "x-content-type-options": [
      "nosniff"
    ],
    "strict-transport-security": [
      "max-age=31536000; includeSubDomains"
    ],
    "access-control-allow-origin": [
      "*"
    ],
    "access-control-allow-methods": [
      "POST,PUT,GET,HEAD,DELETE,OPTIONS"
    ],
    "access-control-allow-credentials": [
      "true"
    ],
    "access-control-expose-headers": [
      "access-control-allow-credentials,access-control-allow-methods,access-control-allow-origin,content-length,date,etag,last-modified,opc-client-info,opc-content-md5,opc-request-id,strict-transport-security,version-id,x-api-id,x-content-type-options"
    ]
  }
}
```

### Method: getObject()

**Function handler: com.pega.launchpad.oracle.ObjectStorageSyncExample::getObject**

This method will get the base64 encoded content of an object from a specific bucket.

Inputs:

1. bucketName (String): Name of the bucket
2. objectName (String): Key for the object in the bucket

Output:

- String: The base64 encoded object content

## Running unit tests

To successfully run the unit tests locally, you'll need to set an environmental variable:

- privateKeyBase64: the base64-encoded json of your PEM formatted oracle public RSA key
