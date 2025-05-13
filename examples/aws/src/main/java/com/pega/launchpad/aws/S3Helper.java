package com.pega.launchpad.aws;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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

}
