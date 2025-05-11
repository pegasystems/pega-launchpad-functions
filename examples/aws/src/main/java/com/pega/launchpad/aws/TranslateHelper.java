package com.pega.launchpad.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

import java.util.Map;

public class TranslateHelper {

    /**
     * Translate text
     *
     * @param input Must contain accessKeyId, secretAccessKey, sourceLanguageCode, targetLanguageCode, text
     * @return String the translated text
     */
    public static String translateText(Map<String, String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        Region region = Region.US_EAST_1;
        TranslateTextResponse textResponse;
        try (TranslateClient translateClient = TranslateClient.builder()
                .region(region)
                .build()) {

            TranslateTextRequest textRequest = TranslateTextRequest.builder()
                    .sourceLanguageCode(input.getOrDefault("sourceLanguageCode", "en"))
                    .targetLanguageCode(input.getOrDefault("targetLanguageCode", "es"))
                    .text(input.getOrDefault("text", "It's a sunny day today."))
                    .build();

            textResponse = translateClient.translateText(textRequest);
        }
        return textResponse.translatedText();
    }

}
