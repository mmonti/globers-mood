package com.globant.labs.mood.support.jersey;

import com.globant.labs.mood.model.persistent.Attribute;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.sun.jersey.api.representation.Form;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by mmonti on 7/31/14.
 */
public class FeedbackContent extends Form {

    private static final String CAMPAIGN_ID = "campaignId";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";

    private static final String[] RESERVED_KEYS = {CAMPAIGN_ID, EMAIL, TOKEN};

    /**
     *
     */
    public FeedbackContent() {
    }

    /**
     * @param campaignId
     * @param email
     * @param token
     * @param attributes
     */
    public FeedbackContent(final Long campaignId, final String email, final String token, final Set<Attribute> attributes) {
        putSingle(CAMPAIGN_ID, campaignId);
        putSingle(EMAIL, email);
        putSingle(TOKEN, token);

        for (final Attribute attribute : attributes) {
            putSingle(attribute.getKey(), attribute.getValue());
        }
    }

    /**
     * @return
     */
    public Long getCampaignId() {
        return this.getFirst(CAMPAIGN_ID, Long.class);
    }

    /**
     * @return
     */
    public String getEmail() {
        return this.getFirst(EMAIL, String.class);
    }

    /**
     * @return
     */
    public String getToken() {
        return this.getFirst(TOKEN, String.class);
    }

    /**
     * @return
     */
    public Set<Attribute> getAttributes() {
        final Iterable<String> filtered = Iterables.filter(this.keySet(), new Predicate<String>() {
            @Override
            public boolean apply(final String key) {
                return !Iterables.contains(Arrays.asList(RESERVED_KEYS), key);
            }
        });
        final Iterable<Attribute> attributes = Iterables.transform(filtered, new Function<String, Attribute>() {
            @Override
            public Attribute apply(final String key) {
                final String value = String.class.cast(getFirst(key));
                return new Attribute(key, value);
            }
        });
        return Sets.newHashSet(attributes);
    }

}
