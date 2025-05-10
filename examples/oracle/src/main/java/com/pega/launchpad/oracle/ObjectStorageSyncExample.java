package com.pega.launchpad.oracle;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreateBucketDetails;
import com.oracle.bmc.objectstorage.requests.*;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest.Builder;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListBucketsResponse;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class ObjectStorageSyncExample {

    /**
     * Get list of buckets
     * @param inputMap requires authentication keys
     * @return List<Object> a list of BucketSummary objects
     */
    public static List<Object> listBuckets(Map<String,String> inputMap) {
        try {

            SimpleAuthenticationDetailsProvider provider = createProvider(inputMap);

            try (ObjectStorage client =
                         ObjectStorageClient.builder().region(provider.getRegion()).build(provider)) {

                String namespaceName = getNamespace(client);

                Builder listBucketsBuilder =
                        ListBucketsRequest.builder()
                                .namespaceName(namespaceName)
                                .compartmentId(provider.getTenantId());

                List<Object> response = new ArrayList<>();
                String nextToken = null;
                do {
                    listBucketsBuilder.page(nextToken);
                    ListBucketsResponse listBucketsResponse =
                            client.listBuckets(listBucketsBuilder.build());
                    response.addAll(listBucketsResponse.getItems());
                    nextToken = listBucketsResponse.getOpcNextPage();
                } while (nextToken != null);
                return response;
            }

        } catch (Throwable t) {
            System.err.println(t.getMessage());
            throw new RuntimeException(t);
        }

    }

    /**
     * Get object content
     * @param inputMap Requires authentication keys, bucketName, and objectName
     * @return String base64 encoded content of the specified object
     */
    public static String getObject(Map<String,String> inputMap) {
        try {

            SimpleAuthenticationDetailsProvider provider = createProvider(inputMap);

            try (ObjectStorage client =
                         ObjectStorageClient.builder().region(provider.getRegion()).build(provider)) {

                String namespaceName = getNamespace(client);

                GetObjectResponse getResponse =
                        client.getObject(
                                GetObjectRequest.builder()
                                        .namespaceName(namespaceName)
                                        .bucketName(inputMap.get("bucketName"))
                                        .objectName(inputMap.get("objectName"))
                                        .build());

                // stream contents should match the file uploaded
                String content;

                try (final java.io.InputStream fileStream = getResponse.getInputStream()) {
                    byte[] b = fileStream.readAllBytes();
                    content = Base64.getEncoder().encodeToString(b);
                    return content;
                } // try-with-resources automatically closes fileStream
            }

        } catch (Throwable t) {
            System.err.println(t.getMessage());
            throw new RuntimeException(t);
        }

    }

    /**
     * Create an object
     * @param inputMap Requires authentication keys, bucketName, objectName, and objectBase64
     * @return Object the PutObjectResponse object
     */
    public static Object putObject(Map<String,String> inputMap)  {
        try {

            SimpleAuthenticationDetailsProvider provider = createProvider(inputMap);

            try (ObjectStorage client =
                         ObjectStorageClient.builder().region(provider.getRegion()).build(provider)) {

                String namespaceName = getNamespace(client);

                String objectBase64 = inputMap.get("objectBase64");
                byte [] b = Base64.getDecoder().decode(objectBase64);
                ByteArrayInputStream is = new ByteArrayInputStream(b);

                return client.putObject(PutObjectRequest.builder()
                                .namespaceName(namespaceName)
                                .bucketName(inputMap.get("bucketName"))
                                .objectName(inputMap.get("objectName"))
                                .putObjectBody(is)
                                .build());
            }

        } catch (Throwable t) {
            System.err.println(t.getMessage());
            throw new RuntimeException(t);
        }

    }

    /**
     * Create a bucket
     * @param inputMap Requires authentication keys, compartmentId (usually your tenancy/tenantId), bucketName
     * @return Object the CreateBucketResponse object
     */
    public static Object createBucket(Map<String,String> inputMap)  {
        try {

            SimpleAuthenticationDetailsProvider provider = createProvider(inputMap);

            try (ObjectStorage client =
                         ObjectStorageClient.builder().region(provider.getRegion()).build(provider)) {

                String namespaceName = getNamespace(client);

                CreateBucketDetails createSourceBucketDetails =
                        CreateBucketDetails.builder().compartmentId(inputMap.get("compartmentId")).name(inputMap.get("bucketName")).build();
                CreateBucketRequest createSourceBucketRequest =
                        CreateBucketRequest.builder()
                                .namespaceName(namespaceName)
                                .createBucketDetails(createSourceBucketDetails)
                                .build();
                return client.createBucket(createSourceBucketRequest);
            }

        } catch (Throwable t) {
            System.err.println(t.getMessage());
            throw new RuntimeException(t);
        }

    }

    private static SimpleAuthenticationDetailsProvider createProvider(Map<String,String> inputMap) {
        String region = inputMap.getOrDefault("region", "us-ashburn-1");
        String privateKeyBase64 = inputMap.get("privateKeyBase64");
        String privateKey = new String(Base64.getDecoder().decode(privateKeyBase64));

        return SimpleAuthenticationDetailsProvider.builder()
                .userId(inputMap.get("userId"))
                .tenantId(inputMap.get("tenantId"))
                .region(Region.fromRegionCodeOrId(region))
                .fingerprint(inputMap.get("fingerprint"))
                .privateKeySupplier(new StringPrivateKeySupplier(privateKey)).build();
    }

    private static String getNamespace(ObjectStorage client) {
        GetNamespaceResponse namespaceResponse =
                client.getNamespace(GetNamespaceRequest.builder().build());
        return namespaceResponse.getValue();
    }
}
