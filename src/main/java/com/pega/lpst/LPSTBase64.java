package com.pega.lpst;

import java.util.Base64;
import java.util.Map;

/**
 * Simple passthrough to java base64 decode and encode methods
 */
public class LPSTBase64 {
    /**
     * Decode a base64 string to text
     * @param inputMap Expected to contain a key of "inputText" and a value of the base64 string to decode
     * @return Object The decoded String object
     */
    public static Object decode(Map<Object,Object> inputMap) {
        String inputText = (String)inputMap.get("inputText");
        return new String(Base64.getDecoder().decode(inputText));
    }

    /**
     * Encode a string to base64
     * @param inputMap Expected to contain a key of "inputText" and a value of the string to encode
     * @return Object The encoded String object
     */
    public static Object encode(Map<Object,Object> inputMap) {
        String inputText = (String)inputMap.get("inputText");
        return new String(Base64.getEncoder().encode(inputText.getBytes()));
    }
}