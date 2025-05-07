package com.pega.launchpad.docusign;

import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import com.docusign.esign.model.EnvelopeSummary;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
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

        String clientId = prop.getProperty("clientId");
        if ("replace".equalsIgnoreCase(clientId)) {
            System.out.println("authentication options not provided, forcing successful test");
            return;
        }

        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));
        String base64 = new String(Base64.encode(privateKeyBytes));


        Map<String,String> inputMap = new HashMap<>();

        inputMap.put("clientId",  prop.getProperty("clientId"));
        inputMap.put("userId", prop.getProperty("userId"));
        inputMap.put("privateKeyBase64", base64);

        inputMap.put("subject", "test subject");
        inputMap.put("status", "sent");
        inputMap.put("signerEmail", "tim.miranda@gmail.com");
        inputMap.put("signerName", "Tim Miranda");
        inputMap.put("documentContent", "dGhpcyBpcyBhIHRlc3Q=");
        inputMap.put("documentName", "doc1.txt");
        inputMap.put("documentExtension", "txt");

        EnvelopeSummary m = (EnvelopeSummary)Docusign.createEnvelope(inputMap);

        inputMap.put("envelopeId", m.getEnvelopeId());
        Envelope e = (Envelope)Docusign.getEnvelope(inputMap);
        EnvelopeDocumentsResult edr = (EnvelopeDocumentsResult)Docusign.getEnvelopeDocuments(inputMap);

        for (EnvelopeDocument ed : edr.getEnvelopeDocuments()) {
            inputMap.put("documentId", ed.getDocumentId());
            String result = Docusign.getDocument(inputMap);

        }


    }

}