package com.jgc.tools.validator.filter;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.codehaus.jackson.map.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;

public class FieldsUtils {

    private static PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy strategy = new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy();

    public static String translate(String input) {
        return strategy.translate(input);
    }

}
