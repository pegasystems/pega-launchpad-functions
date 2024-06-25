package com.pega.launchpad;

import org.jetbrains.annotations.NotNull;

import java.util.*;
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
    public static Boolean regex(@NotNull Map<String,String> inputMap) {
        String regex = inputMap.get("regex");
        if (regex == null) throw new IllegalArgumentException("regex cannot be null");
        String text = inputMap.get("text");
        if (text == null) throw new IllegalArgumentException("text cannot be null");
        boolean caseInsensitive = inputMap.containsKey("caseInsensitive") && Boolean.parseBoolean(inputMap.get("caseInsensitive"));

        Pattern p = Pattern.compile(regex, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);
        Matcher m = p.matcher(text);

        return m.find();
    }

    /**
     * Wrapper for the java String.format() method, to substitute values into a string and return the formatted string
     * @param inputMap Must contain key 'format' with a valid java String [format](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Formatter.html#syntax), a key 'values' with a comma-delimited string of values to use, and optionally a key 'locale' for the specific java [locale string](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Locale.html) to use for formatting.
     * @return String the formatted string result
     */
    public static String format(@NotNull Map<String,String> inputMap) {
        String text = inputMap.get("format");
        if (text == null) throw new IllegalArgumentException("text cannot be null");
        String values = inputMap.get("values");
        if (values == null) throw new IllegalArgumentException("values cannot be null");

        String locale = inputMap.get("locale");
        Locale l = locale != null ? Locale.forLanguageTag(locale) : Locale.getDefault();

        List<String> valueList = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(values, ",");

        while (st.hasMoreTokens()) {
            valueList.add(st.nextToken());
        }

        return String.format(l, text, valueList.toArray());
    }
}
