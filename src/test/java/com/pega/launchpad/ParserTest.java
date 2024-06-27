package com.pega.launchpad;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void fromDelimitedText() {
        Map<String,String> m = new HashMap<>();
        m.put("text", "tim,frank,parag");
        List<Map<?,?>> l = Parser.fromDelimitedText(m);
        assertNotNull(l);
        assertEquals(3, l.size());

        assertTrue(l.get(0).containsKey("token"));
        assertEquals("tim", l.get(0).get("token"));
        assertTrue(l.get(1).containsKey("token"));
        assertEquals("frank", l.get(1).get("token"));
        assertTrue(l.get(2).containsKey("token"));
        assertEquals("parag", l.get(2).get("token"));
    }

    @Test
    void fromCsv() {
        Map<String,String> m = new HashMap<>();
        m.put("csv", "name,city,state\nTim,Chelmsford,MA\nGabe,Arlington,MA\n");
        List<Map<?,?>> l = Parser.fromCsv(m);
        assertNotNull(l);
        assertEquals(2, l.size());

        assertTrue(l.get(0).containsKey("name"));
        assertTrue(l.get(0).containsKey("city"));
        assertTrue(l.get(0).containsKey("state"));
        assertEquals("Tim", l.get(0).get("name"));
        assertEquals("Chelmsford", l.get(0).get("city"));
        assertEquals("MA", l.get(0).get("state"));

        assertTrue(l.get(1).containsKey("name"));
        assertTrue(l.get(1).containsKey("city"));
        assertTrue(l.get(1).containsKey("state"));
        assertEquals("Gabe", l.get(1).get("name"));
        assertEquals("Arlington", l.get(1).get("city"));
        assertEquals("MA", l.get(1).get("state"));

    }

    @Test
    void fromJsonObject() {
        Map<String,String> input = new HashMap<>();
        input.put("json", "{\"name\":\"tim\", \"city\":\"chelmsford\", \"account\":{\"number\":\"1234\"}}");
        Map<?,?> output = Parser.fromJsonObject(input);
        assertEquals("tim", output.get("name"));
        assertEquals("chelmsford", output.get("city"));
        assertEquals("1234", ((Map<?,?>)output.get("account")).get("number"));
    }

    @Test
    void fromJsonArray() {
        Map<String,String> input = new HashMap<>();
        input.put("json", "[{\"name\":\"tim\", \"city\":\"chelmsford\", \"account\":{\"number\":\"1234\"}},{\"name\":\"gabe\", \"city\":\"arlington\", \"account\":{\"number\":\"5678\"}}]");
        List<Map<?,?>> output = Parser.fromJsonArray(input);
        Map<?,?> firstRecord = output.get(0);
        assertEquals("tim", firstRecord.get("name"));
        assertEquals("chelmsford", firstRecord.get("city"));
        assertEquals("1234", ((Map<?,?>)firstRecord.get("account")).get("number"));
        Map<?,?> secondRecord = output.get(1);
        assertEquals("gabe", secondRecord.get("name"));
        assertEquals("arlington", secondRecord.get("city"));
        assertEquals("5678", ((Map<?,?>)secondRecord.get("account")).get("number"));
    }
}