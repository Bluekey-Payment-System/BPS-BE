package com.github.bluekey.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameExtractor {
    private static final String regex = "[a-zA-Z가-힣0-9]+";

    private NameExtractor() {}

    public static List<String> getExtractedNames(String name) {
        List<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        while (matcher.find()) {
            String word = matcher.group();
            words.add(word);
        }
        return words;
    }
}
