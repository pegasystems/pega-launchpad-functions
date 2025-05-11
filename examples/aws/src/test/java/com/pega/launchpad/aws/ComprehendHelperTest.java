package com.pega.launchpad.aws;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ComprehendHelperTest {

    @Test
    void detectKeyPhrases() {
        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) return;

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);
        inputMap.put("text", "Pega is The Enterprise Transformation Company™ that helps organizations Build for Change® with enterprise AI decisioning and workflow automation. Many of the world’s most influential businesses rely on our platform to solve their most pressing challenges, from personalizing engagement to automating service to streamlining operations. Since 1983, we’ve built our scalable and flexible architecture to help enterprises meet today’s customer demands while continuously transforming for tomorrow.");
        inputMap.put("minimumScore", "0.999");
        System.out.println(new Gson().toJson(ComprehendHelper.detectKeyPhrases(inputMap)));
    }
}