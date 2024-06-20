package com.pega.lpst;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Useful examples of string parsing for use with your app logic
 */
public class LPSTParser {

    private static final int MAX_COLUMNS = 10;

    /**
     * Convert a string containing delimited values into a List Of Cases for use in app logic
     * @param inputMap Should contain a key "text" with a value of delimited string like "a,b,c,d". Optionally can contain key "delim" that specifies the delimiter being used
     * @return Object a List of LPSTObject objects, where each object has their Name field set to a token from the delimited string.
     */
    public static Object textToListOfObjects(Map<Object,Object> inputMap) {
        if (inputMap == null) throw new IllegalArgumentException("inputMap must have value");
        String text = (String)inputMap.get("inputText");
        if (text == null) throw new IllegalArgumentException("inputMap must have 'text' value");
        String delim = (String)inputMap.get("delim");
        if (delim == null) delim = ",";

        List<LPSTObject> list = new ArrayList<LPSTObject>();

        StringTokenizer st = new StringTokenizer(text, delim);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            LPSTObject o = new LPSTObject();
            o.Name = token;
            list.add(o);
        }

        return list;
    }

    /**
     * Parse a comma-separated-value string into a List of Objects.
     * @param inputMap Must contain key 'csv' with a value of a csv string. Maximum of 10 columns supported.
     * @return Object a List of LPSTObject objects, where each object has their Field1,Field2,..,Fieldn member fields set to the value of each column.
     */
    public static Object csvToListOfObjects(Map<Object,Object> inputMap) {
        if (inputMap == null) throw new IllegalArgumentException("inputMap must have value");
        String csv = (String)inputMap.get("csv");
        if (csv == null) throw new IllegalArgumentException("inputMap must have 'csv' value");

        List<Object> l = new ArrayList<Object>();
        try {
            for (CSVRecord record : CSVFormat.DEFAULT.builder().setHeader().build().parse(new StringReader(csv))) {
                
                int recordSize = record.size();
                
                if (recordSize > MAX_COLUMNS) throw new IllegalArgumentException("record has than maximum supported columns (max: " + MAX_COLUMNS + ", record: " + recordSize + "): " + record.toString());

                LPSTObject obj = new LPSTObject();
                obj.NumActiveFields = recordSize;

                obj.Field1 = record.get(0);
                if (obj.NumActiveFields > 1) {
                    obj.Field2 = record.get(1);
                    if (obj.NumActiveFields > 2) {
                        obj.Field3 = record.get(2);
                        if (obj.NumActiveFields > 3) {
                            obj.Field4 = record.get(3);
                            if (obj.NumActiveFields > 4) {
                                obj.Field5 = record.get(4);
                                if (obj.NumActiveFields > 5) {
                                    obj.Field6 = record.get(5);
                                    if (obj.NumActiveFields > 6) {
                                        obj.Field7 = record.get(6);
                                        if (obj.NumActiveFields > 7) {
                                            obj.Field8 = record.get(7);
                                            if (obj.NumActiveFields > 8) {
                                                obj.Field9 = record.get(8);
                                                if (obj.NumActiveFields > 9) {
                                                    obj.Field10 = record.get(9);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l.add(obj);

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return l;
    }
}
