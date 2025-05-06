package com.pega.launchpad.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectKeyPhrasesRequest;
import software.amazon.awssdk.services.comprehend.model.DetectKeyPhrasesResponse;
import software.amazon.awssdk.services.comprehend.model.KeyPhrase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprehendHelper {

    /**
     * Detect key phrases in specific text
     * @param input Requires accessKeyId,secretAccessKey,text
     * @return
     */
    public static Object detectKeyPhrases(Map<String,String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));


        try (ComprehendClient comClient = ComprehendClient.builder()
                .region(Region.US_EAST_1)
                .build()) {

            DetectKeyPhrasesRequest detectKeyPhrasesRequest = DetectKeyPhrasesRequest.builder()
                    .text(input.get("text"))
                    .languageCode(input.getOrDefault("languageCode", "en"))
                    .build();

            DetectKeyPhrasesResponse detectKeyPhrasesResult = comClient.detectKeyPhrases(detectKeyPhrasesRequest);

            float minimumScore = Float.parseFloat(input.getOrDefault("minimumScore", "0"));

            List<Map<String,String>> responseList = new ArrayList<>();
            for (KeyPhrase phrase : detectKeyPhrasesResult.keyPhrases()) {
                if (phrase.score() >= minimumScore) {
                    Map<String, String> keyPhraseMap = new HashMap<>();
                    keyPhraseMap.put("keyPhrase", phrase.text());
                    keyPhraseMap.put("score", Float.toString(phrase.score()));
                    responseList.add(keyPhraseMap);
                }
            }
            return responseList;

        }
    }

}
