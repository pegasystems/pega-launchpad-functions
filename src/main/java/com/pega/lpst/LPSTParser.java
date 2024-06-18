package com.pega.lpst;

import java.util.*;

/**
 * Useful examples of string parsing for use with your app logic
 */
public class LPSTParser {

    /**
     * Convert a string containing delimited values into a List Of Cases for use in app logic
     * @param inputMap Should contain a key "text" with a value of delimited string like "a,b,c,d". Optionally can contain key "delim" that specifies the delimiter being used
     * @return Object a List of LPSTObject objects, where each object has their Name field set to a token from the delimited string.
     */
    public static Object textToListOfObjects(Map<Object,Object> inputMap) {
        if (inputMap == null) throw new AssertionError("inputMap must have value");
        String text = (String)inputMap.get("inputText");
        if (text == null) throw new AssertionError("inputMap must have 'text' value");
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
}
