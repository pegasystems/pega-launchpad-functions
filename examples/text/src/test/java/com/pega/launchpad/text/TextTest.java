package com.pega.launchpad.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    void regex() {
        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("regex", "a*b");
        inputMap.put("text", "aaaaab");
        Assertions.assertTrue(Text.regex(inputMap));
    }

    @Test
    void format() {
        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("format", "This is a formatted string containing a value like %s and a decimal value like %s and even a%nsecond line with another value like %s!");
        inputMap.put("values", "Some Value,20.0,Another Value");
        assertEquals("This is a formatted string containing a value like Some Value and a decimal value like 20.0 and even a" + java.lang.System.lineSeparator() + "second line with another value like Another Value!", Text.format(inputMap));
    }
}