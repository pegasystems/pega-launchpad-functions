package com.pega.lpst;

import com.google.gson.Gson;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.Base64;

/**
 * Example of filling in PDF forms
 */
public class PDF {

    /**
     * Given a PDF form, and a map of fields and values, set those fields in the form to those values, and return the resulting PDF document
     * @param inputMap Expects key of 'inputForm' with value of a PDF document encoded in base64, and a key of 'fieldJson' with a value that is a json string containing the fully-qualified field names and the values to set them to. Example: {"sampleField": "Value for sampleField", "fieldsContainer.nestedSampleField": "Value for nestedSampleField"}
     * @return String Base64 encoded PDF document, with fields filled in
     */
    public static String setFields(@NotNull Map<String, String> inputMap) {
        String inputForm = inputMap.get("inputForm");
        if (inputForm == null) throw new IllegalArgumentException("inputForm cannot be null");

        String fieldJson = inputMap.get("fieldJson");
        if (fieldJson == null) throw new IllegalArgumentException("fieldJson cannot be null");

        @SuppressWarnings("unchecked") Map<String, String> fieldMap = (Map<String, String>) new Gson().fromJson(fieldJson, Map.class);

        try (PDDocument pdfDocument = Loader.loadPDF(Base64.getDecoder().decode(inputForm))) {
            // get the document catalog
            PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

            // as there might not be an AcroForm entry a null check is necessary
            if (acroForm != null) {
                for (String fieldKey : fieldMap.keySet()) {
                    String value = fieldMap.get(fieldKey);
                    PDField field = acroForm.getField(fieldKey);
                    if (field instanceof PDCheckBox) {
                        @SuppressWarnings("all") PDCheckBox checkboxField = (PDCheckBox)field;
                        if (Boolean.parseBoolean(value)) {
                            checkboxField.check();
                        } else {
                            checkboxField.unCheck();
                        }
                    } else {
                        field.setValue(value);
                    }
                }
            }

            // Save and close the filled out form.
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                pdfDocument.save(baos);
                return Base64.getEncoder().encodeToString(baos.toByteArray());
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
