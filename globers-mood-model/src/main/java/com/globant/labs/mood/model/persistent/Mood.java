package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public enum Mood {

    VERY_BAD(-2),
    BAD(-1),
    NEUTRAL(0),
    GOOD(1),
    VERY_GOOD(2);

    private static final Map<String, Mood> LOOKUP = new HashMap<String, Mood>();

    private int value;

    static {
        for (Mood currentMoodValue : EnumSet.allOf(Mood.class)) {
            LOOKUP.put(String.valueOf(currentMoodValue.getValue()), currentMoodValue);
        }
    }

    private Mood(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Mood fromString(final String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        final Mood moodValue = LOOKUP.get(value);
        if (moodValue == null) {
            throw new IllegalArgumentException("The value parameter is invalid=[" + value + "]");
        }
        return moodValue;
    }
}
