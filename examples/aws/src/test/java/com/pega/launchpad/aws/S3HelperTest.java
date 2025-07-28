package com.pega.launchpad.aws;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.utils.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


class S3HelperTest {

    @Test
    void function() {
        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) return;

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);

        System.out.println(S3Helper.listBuckets(inputMap));

        String bucketName = "newbucket" + System.currentTimeMillis();
        inputMap.put("bucketName", bucketName);
        System.out.println(S3Helper.createBucket(inputMap));

        String fileName = "someobject" + System.currentTimeMillis();
        inputMap.put("objectKey", fileName);
        inputMap.put("objectBase64", "dGhpcyBpcyBhIHRlc3Q=");
        System.out.println(S3Helper.putObject(inputMap));

        inputMap.remove("objectBase64");

        System.out.println(S3Helper.getObject(inputMap));
    }

    @Test
    void presignedURLExample() {
        Map<String, String> inputMap = setupS3();
        if (inputMap == null) return;

        //  use HTTP to upload a file
        String newFileContent = "this is an uploaded text file";
        String newFileName= "someobject" + System.currentTimeMillis();
        inputMap.put("objectKey", newFileName);

        String uploadUrl = S3Helper.getPresignedURLForUpload(inputMap);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            final HttpResponse<Void> response = httpClient.send(requestBuilder
                            .uri(new URL(uploadUrl).toURI())
                            .PUT(HttpRequest.BodyPublishers.ofString(newFileContent))
                            .build(),
                    HttpResponse.BodyHandlers.discarding());
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        // Get a presigned url to download that same file from s3
        String url = S3Helper.getPresignedURLForObject(inputMap);

        // Use HTTP to retrieve the file
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // Capture the response body to a byte array.

        requestBuilder = HttpRequest.newBuilder();
        httpClient = HttpClient.newHttpClient();
        try {
            URL presignedUrl = new URL(url);
            HttpResponse<InputStream> response = httpClient.send(requestBuilder
                            .uri(presignedUrl.toURI())
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofInputStream());

            IoUtils.copy(response.body(), byteArrayOutputStream);

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Downloaded file contents: " + byteArrayOutputStream.toString());

    }

    private static Map<String, String> setupS3() {
        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) return null;

        // First, get access keys needed to create a bucket and create a file
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);

        // Create a bucket we can use for testing
        String bucketName = "newbucket" + System.currentTimeMillis();
        inputMap.put("bucketName", bucketName);
        S3Helper.createBucket(inputMap);
        return inputMap;
    }
}