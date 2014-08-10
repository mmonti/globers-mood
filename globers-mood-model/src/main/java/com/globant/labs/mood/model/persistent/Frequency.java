package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;
import com.google.appengine.repackaged.org.joda.time.DateTime;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmonti on 8/8/14.
 */
public enum Frequency {

    ONCE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    private static final Map<String, Frequency> LOOKUP = new HashMap<String, Frequency>();

    static {
        for (final Frequency frequency : EnumSet.allOf(Frequency.class)) {
            LOOKUP.put(String.valueOf(frequency.name()), frequency);
        }
    }

    public static Frequency fromString(final String name) {
        Preconditions.checkNotNull(name, "name cannot be null");
        final Frequency frequency = LOOKUP.get(name);
        if (frequency == null) {
            throw new IllegalArgumentException("The name parameter is invalid=[" + name + "]");
        }
        return frequency;
    }



    public Date getScheduleDate(final Date date) {
        return getDate(date, 1);
    }

    public Date getExpirationDate(final Date date) {
        return getDate(date, 2);
    }

    public Date getDate(final Date date, final int increment) {
        final DateTime dateTime = new DateTime(date);
        switch (this) {
            case ONCE:
                return date;
            case DAILY:
                return dateTime.plusDays(increment).toDate();
            case WEEKLY:
                return dateTime.plusWeeks(increment).toDate();
            case MONTHLY:
                return dateTime.plusMonths(increment).toDate();
            case YEARLY:
                return dateTime.plusYears(increment).toDate();
            default:
                return null;
        }
    }
}
