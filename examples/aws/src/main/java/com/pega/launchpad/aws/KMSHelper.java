package com.pega.launchpad.aws;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

import java.util.Base64;
import java.util.Map;

public class KMSHelper {

    /**
     * Encrypt text
     *
     * @param input Must contain accessKeyId, secretAccessKey, keyId, text
     * @return String the encrypted value in base64
     */
    public static String encrypt(Map<String, String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        SdkBytes myBytes = SdkBytes.fromUtf8String(input.get("text"));

        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(input.get("keyId"))
                .plaintext(myBytes)
                .build();

        try (KmsClient client = KmsClient.builder().region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id()))).build()) {
            EncryptResponse er = client.encrypt(encryptRequest);
            return Base64.getEncoder().encodeToString(er.ciphertextBlob().asByteArray());
        }
    }

    /**
     * Decrypt text
     *
     * @param input Must contain accessKeyId, secretAccessKey, keyId, text
     * @return String the decrypted text
     */
    public static String decrypt(Map<String, String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        SdkBytes myBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(input.get("text")));

        DecryptRequest decryptRequest = DecryptRequest.builder().keyId(input.get("keyId")).ciphertextBlob(myBytes).build();

        try (KmsClient client = KmsClient.builder().region(Region.of(input.getOrDefault("region", Region.US_EAST_1.id()))).build()) {
            DecryptResponse dr = client.decrypt(decryptRequest);
            return dr.plaintext().asUtf8String();
        }
    }

}
