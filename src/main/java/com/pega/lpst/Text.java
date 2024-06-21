package com.pega.lpst;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Text utilities for use by Pega Launchpad applications
 */
public class Text {

    /**
     * Evaluate a regular expression against a string, to find any matches
     * @param inputMap Must contain key 'regex' with regular expression to compile, key 'text' with the string to evaluate against the pattern, and optionally key 'caseInsensitive' as true or false for the Pattern behavior
     * @return Boolean true if pattern found in text, false otherwise
     */
    public static Boolean regex(Map<String,String> inputMap) {
        String regex = inputMap.get("regex");
        if (regex == null) throw new IllegalArgumentException("regex cannot be null");
        String text = inputMap.get("text");
        if (text == null) throw new IllegalArgumentException("text cannot be null");
        boolean caseInsensitive = inputMap.containsKey("caseInsensitive") && Boolean.parseBoolean(inputMap.get("caseInsensitive"));

        Pattern p = Pattern.compile(regex, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);
        Matcher m = p.matcher(text);

        return m.find();
    }
}
