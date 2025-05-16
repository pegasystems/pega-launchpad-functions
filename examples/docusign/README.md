# Docusign Java SDK integration example

This java code can be imported into a Function rule in your application for simple Docusign e-signature integration.

<!-- TOC -->
* [Docusign Java SDK integration example](#docusign-java-sdk-integration-example)
  * [Pre-requisities](#pre-requisities)
  * [eSignature API general documentation](#esignature-api-general-documentation)
  * [Authentication Information](#authentication-information)
    * [createEnvelope()](#createenvelope)
    * [getEnvelope()](#getenvelope)
    * [getEnvelopeDocuments()](#getenvelopedocuments)
    * [getDocument()](#getdocument)
  * [Running unit tests](#running-unit-tests)
<!-- TOC -->

## Pre-requisities

You will need to set up authentication and authorization to use the docusign APIs. More information can be found here: https://apps-d.docusign.com/admin/apps-and-keys

Details on the authentication used by this code, JWT Grant with remote signing, can be found here: https://developers.docusign.com/platform/auth/jwt-get-token/

You also need to obtain one-time user consent to use your app key with their userid programmatically. See https://developers.docusign.com/platform/auth/consent/obtaining-individual-consent/ for instructions.

An example URL you can use to self-consent for your user:

```https://account-d.docusign.com/oauth/auth?response_type=code&scope=signature%20impersonation&client_id=YOURCLIENTID&redirect_uri=https%3A%2F%2Fdevelopers.docusign.com%2Fplatform%2Fauth%2Fconsent```


## eSignature API general documentation

For a list of code examples that use the eSignature API, see the [How-to guides overview](https://developers.docusign.com/docs/esign-rest-api/how-to/) on the Docusign Developer Center.

## Authentication Information

All functions require these inputs to authenticate:

- basePath (String): For development, this can be https://demo.docusign.net/restapi ; otherwise use your production docusign endpoint
- oAuthBasePath (String): For development, this can be https://account-d.docusign.com ; otherwise use your production auth endpoint
- clientId (String): This is your application integration key, see documentation above for more information
- userId (String): This is the hash ID of the user you will authenticate against
- privateKeyBase64 (String): The base64 encoded RSA private key file for your app key

### createEnvelope()

This example function will create a new Envelope and return the summary status of it. Some of the settings can 
be configured as inputs, and some are hardcoded in the java method and need to be customized for your use cases.

Inputs:

- subject (String): email subject
- status (String): what initial status to use for the envelope
- signerEmail (String): the email address for signer. This will also be the recipient.
- signerName (String): Full name of signer
- documentContent (String): base64 encoded document to add to envelope
- documentName (String): filename of the document ("doc1.txt")
- documentExtension (String): the extension of the document ("txt")
- xPosition (String): Horizontal offset from left side for the signing area (72 dots per inch). Default is 72 (1 inch)
- yPosition (String): Vertical offset from top for the signing area (72 dots per inch). Default is 720 (10 inches)

- Sample json output (use json transform to map back to your object data model):

```
{
  "envelopeId": "0d077e5c-ecaa-42c4-a60b-7b437d26c0ad",
  "status": "sent",
  "statusDateTime": "2025-05-02T16:41:13.7800000Z",
  "uri": "/envelopes/0d077e5c-ecaa-42c4-a60b-7b437d26c0ad"
}
```

### getEnvelope()

This example function, given an envelope id, will return the Envelope json object containing the current status and details for the given envelope.

Inputs:

- envelopeId (String): The id returned by the createEnvelope() method

Sample json output (use json transform to map back to your object data model):

```
{
"allowComments": "true",
"allowMarkup": "false",
"allowReassign": "true",
"allowViewHistory": "true",
"attachmentsUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/attachments",
"autoNavigation": "true",
"burnDefaultTabData": "false",
"certificateUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/documents/certificate",
"createdDateTime": "2025-05-02T15:40:39.5870000Z",
"customFieldsUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/custom_fields",
"documentsCombinedUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/documents/combined",
"documentsUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/documents",
"emailSubject": "Please sign this document set",
"enableWetSign": "true",
"envelopeId": "cc2d50cb-acb6-4cd5-985c-2c6699b6683e",
"envelopeIdStamping": "true",
"envelopeLocation": "current_site",
"envelopeMetadata": {
"allowAdvancedCorrect": "true",
"allowCorrect": "true",
"enableSignWithNotary": "true"
},
"envelopeUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e",
"expireAfter": "120",
"expireDateTime": "2025-08-30T15:40:40.3970000Z",
"expireEnabled": "true",
"hasComments": "false",
"hasFormDataChanged": "false",
"initialSentDateTime": "2025-05-02T15:40:40.3970000Z",
"is21CFRPart11": "false",
"isDynamicEnvelope": "false",
"isSignatureProviderEnvelope": "false",
"lastModifiedDateTime": "2025-05-02T15:40:39.6000000Z",
"notificationUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/notification",
"purgeState": "unpurged",
"recipientsUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/recipients",
"sender": {
"accountId": "ACCOUNT",
"email": "EMAIL",
"ipAddress": "IPADDRESS",
"userId": "USERID",
"userName": "USERNAME"
},
"sentDateTime": "2025-05-02T15:40:40.3970000Z",
"signerCanSignOnMobile": "true",
"signingLocation": "online",
"status": "sent",
"statusChangedDateTime": "2025-05-02T15:40:40.3970000Z",
"templatesUri": "/envelopes/cc2d50cb-acb6-4cd5-985c-2c6699b6683e/templates",
"uSigState": "esign"
}
```

### getEnvelopeDocuments()

This method, given an envelope ID, will return the EnvelopeDocumentResult json object that contains details about each document in the envelope.

Inputs:

- envelopeId: The ID of the envelope returned by createEnvelope() API

Sample json output (use json transform to map to your object data model):

```
{
  "envelopeDocuments": [
    {
      "authoritativeCopy": "false",
      "availableDocumentTypes": [
        {
          "isDefault": "true",
          "type": "electronic"
        }
      ],
      "display": "inline",
      "documentId": "1",
      "documentIdGuid": "a1896dbf-0eb1-4778-9294-c040fafab221",
      "includeInDownload": "true",
      "name": "doc1.txt",
      "order": "1",
      "pages": [
        {
          "dpi": "72",
          "height": "792",
          "pageId": "637be6b1-bef6-4589-955f-475e7f68671f",
          "sequence": "1",
          "width": "612"
        }
      ],
      "signerMustAcknowledge": "no_interaction",
      "templateRequired": "false",
      "type": "content",
      "uri": "/envelopes/2dbb5b7b-2980-431e-a757-80899bdd7b2f/documents/1"
    },
    {
      "authoritativeCopy": "false",
      "availableDocumentTypes": [
        {
          "isDefault": "true",
          "type": "electronic"
        }
      ],
      "display": "inline",
      "documentId": "certificate",
      "documentIdGuid": "4a599879-1ea2-4527-905b-f9550d7d5ab9",
      "includeInDownload": "true",
      "name": "Summary",
      "order": "999",
      "signerMustAcknowledge": "no_interaction",
      "templateRequired": "false",
      "type": "summary",
      "uri": "/envelopes/2dbb5b7b-2980-431e-a757-80899bdd7b2f/documents/certificate"
    }
  ],
  "envelopeId": "2dbb5b7b-2980-431e-a757-80899bdd7b2f"
}
```

### getDocument()

This method, given an envelope id and document id, will return the content of the document as base64 encoded bytes.

Inputs:

- envelopeId: The id of the envelope returned from createEnvelope()
- documentId: The id of the document you want (ex: "1", "2", etc), obtained from a getEnvelopeDocuments() API call

Outputs:

- String: The base64 encoded content of the document

## Running unit tests

You'll need to create and configure the following files in this project:

1. src/test/resources/DocusignTest.properties

This should contain three settings:

```
clientId=The integration key (application key)
userId=The hash identifier for the docusign user that you will authenticate with
rsaKeyFile=The location of the rsa private key you downloaded from your docusign app integration configuration.
```

2. src/test/resources/private.key

This should contain the RSA private key text from your application key setup in docusign
