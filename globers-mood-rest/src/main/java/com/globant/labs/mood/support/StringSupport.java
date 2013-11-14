package com.globant.labs.mood.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class StringSupport {

    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");

    /**
     *
     * @param placeHolder
     * @param parameters
     * @return
     */
    public static String on(final String placeHolder, final Object ... parameters) {
        final StringBuffer buffer = new StringBuffer();
        final Matcher matcher = pattern.matcher(placeHolder);

        int index = 0;
        while (matcher.find()) {
            matcher.appendReplacement(buffer, String.valueOf(parameters[index]));
            index++;
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

}
