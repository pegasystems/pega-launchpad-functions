package com.pega.launchpad.aws;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Base64;
import java.util.Map;

public class S3Helper {

    /**
     * List buckets in account
     *
     * @param input Must contain accessKeyId, secretAccessKey
     * @return Object of class software.amazon.awssdk.services.s3.model.ListBucketsResponse
     */
    public static Object listBuckets(Map<String, String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id())))
                .build()) {

            return s3.listBuckets();
        }
    }

    /**
     * Create a bucket
     *
     * @param input Must contain accessKeyId, secretAccessKey, and bucketName
     * @return Object of class software.amazon.awssdk.services.s3.model.CreateBucketResponse
     */
    public static Object createBucket(Map<String, String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        software.amazon.awssdk.services.s3.S3ClientBuilder builder = S3Client.builder();
        builder.region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id())));
        try (S3Client s3 = builder
                .build()) {

            CreateBucketRequest cbr = CreateBucketRequest.builder().bucket(input.get("bucketName")).build();
            return s3.createBucket(cbr);
        }
    }

    /**
     * Put an object into a bucket
     *
     * @param input Must contain accessKeyId, secretAccessKey, bucketName, objectKey, objectBase64
     * @return Object of class software.amazon.awssdk.services.s3.model.PutObjectResponse
     */
    public static Object putObject(Map<String, String> input) {


        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id())))
                .build()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(input.get("bucketName"))
                    .key(input.get("objectKey"))
                    .build();

            return s3.putObject(objectRequest, RequestBody.fromBytes(Base64.getDecoder().decode(input.get("objectBase64"))));
        }
    }

    /**
     * Get an object from a bucket
     *
     * @param input Must contain accessKeyId, secretAccessKey, bucketName, objectKey
     * @return String base64 encoded object content
     */
    public static String getObject(Map<String, String> input)  {


        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id())))
                .build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(input.get("bucketName"))
                    .key(input.get("objectKey"))
                    .build();

            ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(objectRequest);
            byte[] b = bytes.asByteArray();
            return Base64.getEncoder().encodeToString(b);

        }
    }

    /**
     * Get a presigned URL to get an object from a bucket
     *
     * @param input Must contain accessKeyId, secretAccessKey, bucketName, objectKey
     * @return String URL to use to get the specified file
     */
    public static String presignGetObject(Map<String, String> input) {
        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id())))
                .build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(input.get("bucketName"))
                    .key(input.get("objectKey"))
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(1))  // The URL will expire in 1 minute.
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        }
    }

    /**
     * Get a presigned URL to put an object into a bucket
     *
     * @param input Must contain accessKeyId, secretAccessKey, bucketName, objectKey
     * @return String URL to use to put content into the specified bucket and object key
     */
    public static String presignPutObject(Map<String, String> input) {
        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id())))
                .build()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(input.get("bucketName"))
                    .key(input.get("objectKey"))
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(1))  // The URL expires in 1 minute
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String myURL = presignedRequest.url().toString();

            return presignedRequest.url().toExternalForm();
        }
    }

}
