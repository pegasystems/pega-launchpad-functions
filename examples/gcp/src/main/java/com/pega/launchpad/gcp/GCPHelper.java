package com.pega.launchpad.gcp;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class GCPHelper {

    /**
     * Create a bucket
     * @param input requires credentials, bucketName
     * @return Object the BucketInfo object
     */
    public static Object createBucket(Map<String,String> input) throws Exception {


        ServiceAccountCredentials credentials = generateServiceAccountCredentials(input.get("base64EncodedJsonCredentials"));

        // Instantiates a client
        try (Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService()) {
            // Creates the new bucket
            return storage.create(BucketInfo.of(input.get("bucketName"))).asBucketInfo();
        }
    }

    /**
     * Upload a file
     * @param input requires credentials, bucketNAme, objectName, and objectBase64
     * @return Object the BlobInfo object
     */
    public static Object createObject(Map<String,String> input) throws Exception {
        ServiceAccountCredentials credentials = generateServiceAccountCredentials(input.get("base64EncodedJsonCredentials"));

        // Instantiates a client
        try (Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService()) {
            BlobId blobId = BlobId.of(input.get("bucketName"), input.get("objectName"));
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            return storage.createFrom(blobInfo, new ByteArrayInputStream(Base64.getDecoder().decode(input.get("objectBase64")))).asBlobInfo();
        }
    }

    /**
     * Download a file
     * @param input requires credentials, bucketName, objectName
     * @return String the file content in base64
     */
    public static String getObject(Map<String,String> input) throws Exception {
        ServiceAccountCredentials credentials = generateServiceAccountCredentials(input.get("base64EncodedJsonCredentials"));

        // Instantiates a client
        try (Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService()) {
            BlobId blobId = BlobId.of(input.get("bucketName"), input.get("objectName"));
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            return Base64.getEncoder().encodeToString(storage.get(blobId).getContent());
        }
    }

    private static ServiceAccountCredentials generateServiceAccountCredentials(String base64EncodedJsonCredentials) throws IOException {
        if (base64EncodedJsonCredentials == null) throw new IllegalArgumentException("base64EncodedJsonCredentials must be provided");
        byte [] b = Base64.getDecoder().decode(base64EncodedJsonCredentials);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(b)) {
            return ServiceAccountCredentials.fromStream(bais);
        }
    }

}
