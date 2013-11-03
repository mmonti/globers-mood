package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public enum PreferenceKey {

    SENDER_ALIAS("sender.alias"),
    SENDER_MAIL("sender.mail"),
    MAIL_SUBJECT("mail.subject");

    private static final Map<String, PreferenceKey> LOOKUP = new HashMap<String, PreferenceKey>();

    private String value;

    static {
        for (PreferenceKey currentMoodValue : EnumSet.allOf(PreferenceKey.class)) {
            LOOKUP.put(String.valueOf(currentMoodValue.getValue()), currentMoodValue);
        }
    }

    private PreferenceKey(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PreferenceKey fromString(final String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        final PreferenceKey preferenceKey = LOOKUP.get(value);
        if (preferenceKey == null) {
            throw new IllegalArgumentException("The value parameter is invalid=[" + value + "]");
        }
        return preferenceKey;
    }
}
