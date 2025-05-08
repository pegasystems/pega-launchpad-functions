package com.pega.launchpad.aws;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TranslateHelperTest {

    @Test
    void translateText() {
        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) return;

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);
        inputMap.put("text", "it's a sunny day today.");
        inputMap.put("sourceLanguageCode", "en");
        inputMap.put("targetLanguageCode", "es");
        System.out.println(TranslateHelper.translateText(inputMap));

    }
}