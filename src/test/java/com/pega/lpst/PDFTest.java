package com.pega.lpst;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PDFTest {

    @Test
    void setFields() {

        String inputPDF = null;

        File inFile = new File("src/test/resources/com/pega/lpst/FillFormField.pdf");
        try (FileInputStream fis = new FileInputStream(inFile)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte [] bytes = new byte[(int)inFile.length()];
            assertEquals(inFile.length(), fis.read(bytes));
            inputPDF = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(inputPDF);

        String outputPDF = null;
        File outFile = new File("src/test/resources/com/pega/lpst/FillFormFieldExpectedOutput.pdf");
        try (FileInputStream fis = new FileInputStream(outFile)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte [] bytes = new byte[(int)outFile.length()];
            assertEquals(outFile.length(), fis.read(bytes));
            outputPDF = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("inputForm", inputPDF);

        Map<String,String> fieldMapping = new HashMap<>();
        fieldMapping.put("sampleField", "Value for sampleField");
        fieldMapping.put("fieldsContainer.nestedSampleField", "Value for nestedSampleField");
        String json = new Gson().toJson(fieldMapping);
        System.out.println(json);
        inputMap.put("fieldJson", json);

        String result = PDF.setFields(inputMap);

        assertEquals(outputPDF, result);
    }
}