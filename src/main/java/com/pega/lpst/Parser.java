package com.pega.lpst;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

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
     * @return Object a List of TreeMap objects, where each map contains a single mapping: a key called "token" and the value of the token
     */
    public static List<Map<?,?>> fromDelimitedText(@NotNull Map<String,String> inputMap) {
        String text = inputMap.get("text");
        if (text == null) throw new IllegalArgumentException("inputMap must have 'text' value");
        String delim = inputMap.get("delim");
        if (delim == null) delim = ",";

        List<Map<?,?>> list = new ArrayList<>();

        Gson gson = new Gson();

        StringTokenizer st = new StringTokenizer(text, delim);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            Map<?,?> o = gson.fromJson("{\"token\":\"" + token + "\"}", Map.class);
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
    public static List<Map<?,?>> fromCsv(@NotNull Map<String, String> inputMap) {
        String csv = inputMap.get("csv");
        if (csv == null) throw new IllegalArgumentException("inputMap must have 'csv' value");

        List<Map<?,?>> l = new ArrayList<>();

        Gson gson = new Gson();

        try {
            try (CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().build().parse(new StringReader(csv))) {
                for (CSVRecord record : parser) {
                    StringBuilder json = new StringBuilder();
                    json.append("{");
                    Map<String, String> m = record.toMap();
                    Iterator<String> i = m.keySet().iterator();
                    while (i.hasNext()) {
                        String key = i.next();
                        String value = record.get(key);
                        json.append("\"").append(key).append("\":\"").append(value).append("\"");
                        if (i.hasNext()) json.append(",");
                    }
                    json.append("}");
                    Map<?, ?> obj = gson.fromJson(json.toString(), Map.class);
                    l.add(obj);

                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return l;
    }

}
