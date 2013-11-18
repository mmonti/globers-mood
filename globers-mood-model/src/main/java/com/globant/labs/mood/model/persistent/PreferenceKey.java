package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public enum PreferenceKey {

    MAIL_SENDER_ALIAS("sender.alias"),
    MAIL_SENDER("sender.mail"),
    MAIL_SUBJECT("mail.subject"),
    SERVICES_HOST("services.host"),
    SERVICES_FEEDBACK_API("services.feedback.api"),
    SERVICES_SYNCHRONIZE("services.synchronize"),
    SERVICES_SYNCHRONIZE_TIME("services.synchronize.time"),
    DASHBOARD_CAMPAIGN_REFRESH_TIME("dashboard.campaign.refresh.time"),
    DASHBOARD_CAMPAIGN_ITEMS_SIZE("dashboard.campaign.items.size");

    private static final Map<String, PreferenceKey> LOOKUP = new HashMap<String, PreferenceKey>();

    private String value;

    static {
        for (PreferenceKey preferenceKey : EnumSet.allOf(PreferenceKey.class)) {
            LOOKUP.put(String.valueOf(preferenceKey.getValue()), preferenceKey);
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
