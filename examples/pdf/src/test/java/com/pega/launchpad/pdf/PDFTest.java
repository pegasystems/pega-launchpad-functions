package com.pega.launchpad.pdf;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PDFTest {

    @Test
    void setFieldsWithBase64() {

        String inputPDF;

        inputPDF = openPDF("src/test/resources/com/pega/launchpad/FillFormField.pdf");

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("inputForm", inputPDF);

        Map<String,String> fieldMapping = new HashMap<>();
        fieldMapping.put("sampleField", "Value for sampleField");
        fieldMapping.put("fieldsContainer.nestedSampleField", "Value for nestedSampleField");
        String json = new Gson().toJson(fieldMapping);
        inputMap.put("fieldJson", json);

        String result = PDF.setFieldsWithBase64(inputMap);

        String expectedPDF = openPDF("src/test/resources/com/pega/launchpad/FillFormFieldExpectedOutput.pdf");

        //assertEquals(expectedPDF, result);
    }

    private static String openPDF(String fileName) {
        String inputPDF;
        File inFile = new File(fileName);
        try (FileInputStream fis = new FileInputStream(inFile)) {
            byte [] bytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int length;
                byte[] buffer = new byte[1024];// buffer for portion of data from connection
                while ((length = fis.read(buffer)) > -1) {
                    baos.write(buffer, 0, length);
                }
                bytes = baos.toByteArray();
            }
            assertEquals(inFile.length(), bytes.length);
            inputPDF = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inputPDF;
    }

    /*
    @Test
    void setFieldsWithURL() {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("inputURL", "https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/resources/org/apache/pdfbox/examples/interactive/form/FillFormField.pdf?view=co");

        Map<String,String> fieldMapping = new HashMap<>();
        fieldMapping.put("sampleField", "Value for sampleField");
        fieldMapping.put("fieldsContainer.nestedSampleField", "Value for nestedSampleField");
        String json = new Gson().toJson(fieldMapping);
        inputMap.put("fieldJson", json);

        String result = PDF.setFieldsWithURL(inputMap);

        String expectedPDF = openPDF("src/test/resources/com/pega/launchpad/FillFormFieldExpectedOutputFromURL.pdf");
        assertEquals(expectedPDF, result);
    }
    */
}
