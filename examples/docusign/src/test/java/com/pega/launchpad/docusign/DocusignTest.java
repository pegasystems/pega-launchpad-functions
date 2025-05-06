package com.pega.launchpad.docusign;

import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeSummary;
import com.google.gson.Gson;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

class DocusignTest {

    @Test
    void test() throws Exception {
        // Get information fro app.config
        java.util.Properties prop = new Properties();
        String fileName = "src/test/resources/DocusignTest.properties";
        FileInputStream fis = new FileInputStream(fileName);
        prop.load(fis);

        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));

        Map<String,String> inputMap = new HashMap<>();

        inputMap.put("clientId",  prop.getProperty("clientId"));
        inputMap.put("userId", prop.getProperty("userId"));
        inputMap.put("privateKeyBase64", new String(Base64.encode(privateKeyBytes)));

        inputMap.put("subject", "test subject");
        inputMap.put("status", "sent");
        inputMap.put("signerEmail", "tim.miranda@gmail.com");
        inputMap.put("signerName", "Tim Miranda");
        inputMap.put("documentContent", "dGhpcyBpcyBhIHRlc3Q=");
        inputMap.put("documentName", "doc1.txt");
        inputMap.put("documentExtension", "txt");

        if (true != false) return; // comment this out to actually test your integration

        EnvelopeSummary m = (EnvelopeSummary)Docusign.createEnvelope(inputMap);
        System.out.println(m);

        inputMap.put("envelopeId", (String) m.getEnvelopeId());

        Envelope e = Docusign.getEnvelope(inputMap);
        System.out.println(e);
        List<EnvelopeDocument> l = Docusign.getEnvelopeDocuments(inputMap);

        System.out.println(l);

        for (EnvelopeDocument ed : l) {
            inputMap.put("documentId", ed.getDocumentId());
            String result = Docusign.getDocument(inputMap);

        }


    }

}