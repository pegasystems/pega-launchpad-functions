package com.pega.launchpad.docusign;

import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeSummary;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DocusignTest {

    @Test
    void test() throws Exception {
        Map<String,String> inputMap = new HashMap<>();

        EnvelopeSummary es = Docusign.createEnvelope(inputMap);
        System.out.println(new Gson().toJson(es));

        inputMap.put("envelopeId", es.getEnvelopeId());

        Envelope e = Docusign.getEnvelope(inputMap);
        System.out.println(new Gson().toJson(e));

        List<EnvelopeDocument> l = Docusign.getEnvelopeDocuments(inputMap);
        for (EnvelopeDocument ed : l) {
            System.out.println(new Gson().toJson(ed));
            inputMap.put("documentId", ed.getDocumentId());
            String result = Docusign.getDocument(inputMap);
            System.out.println(ed.getDocumentId() + ": " + result.length());
        }
    }
}