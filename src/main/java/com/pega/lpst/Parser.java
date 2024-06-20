package com.pega.lpst;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Useful examples of string parsing for use with your app logic
 */
public class Parser {

    /**
     * Convert a string containing delimited values into a List Of Cases for use in app logic
     *
     * @param inputMap Should contain a key "text" with a value of delimited string like "a,b,c,d". Optionally can contain key "delim" that specifies the delimiter being used
     * @return Object a List of TreeMap objects, where each map contains a single mapping: a key called "Text" and the value of the token
     */
    public static Object fromDelimitedText(Map<Object, Object> inputMap) {
        if (inputMap == null) throw new IllegalArgumentException("inputMap must have value");
        String text = (String) inputMap.get("inputText");
        if (text == null) throw new IllegalArgumentException("inputMap must have 'text' value");
        String delim = (String) inputMap.get("delim");
        if (delim == null) delim = ",";

        List<Object> list = new ArrayList<Object>();

        Gson gson = new Gson();

        StringTokenizer st = new StringTokenizer(text, delim);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String json = "{\"Text\":\"" + token + "\"}";
            Object o = gson.fromJson(json, Object.class);
            list.add(o);
        }

        return list;
    }

    /**
     * Parse a comma-separated-value string into a List of Objects.
     *
     * @param inputMap Must contain key 'csv' with a value of a csv string.
     * @return Object a List of TreeMap objects, where each map contains a column->value mapping, where the key of the mapping is the column header value.
     */
    public static Object fromCsv(Map<Object, Object> inputMap) {
        if (inputMap == null) throw new IllegalArgumentException("inputMap must have value");
        String csv = (String) inputMap.get("csv");
        if (csv == null) throw new IllegalArgumentException("inputMap must have 'csv' value");

        List<Object> l = new ArrayList<Object>();

        Gson gson = new Gson();

        try {
            CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().build().parse(new StringReader(csv));
            try {
                for (CSVRecord record : parser) {

                    String json = "{";
                    Map<String, String> m = record.toMap();
                    Iterator<String> i = m.keySet().iterator();
                    while (i.hasNext()) {
                        String key = i.next();
                        String value = record.get(key);
                        json += "\"" + key + "\":\"" + value + "\"";
                        if (i.hasNext()) json += ",";
                    }
                    json += "}";
                    Object obj = gson.fromJson(json, Object.class);
                    l.add(obj);

                }
            } finally {
                parser.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return l;
    }

}
