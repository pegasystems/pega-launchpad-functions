package com.pega.launchpad.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.util.Map;

public class S3Helper {

    /**
     * List buckets in account
     * @param input Must contain accessKeyId, secretAccessKey
     * @return Object of class software.amazon.awssdk.services.s3.model.ListBucketsResponse
     */
    public static Object listBuckets(Map<String,String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            return s3.listBuckets();
        }
    }

    /**
     * Create a bucket
     * @param input Must contain accessKeyId, secretAccessKey, and bucketName
     * @return Object of class software.amazon.awssdk.services.s3.model.CreateBucketResponse
     */
    public static Object createBucket(Map<String,String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            CreateBucketRequest cbr = CreateBucketRequest.builder().bucket(input.get("bucketName")).build();
            return s3.createBucket(cbr);
        }
    }


}
