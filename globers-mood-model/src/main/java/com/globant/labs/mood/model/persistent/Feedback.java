package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.appengine.repackaged.com.google.common.base.Optional;
import com.google.appengine.repackaged.com.google.common.base.Predicate;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.appengine.repackaged.com.google.common.collect.Iterables.tryFind;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Feedback extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4177075439722820274L;

    /**
     * static instance to of the converter.
     */
    private static final ConvertUtilsBean converter = BeanUtilsBean2.getInstance().getConvertUtils();

    @Unowned
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Campaign campaign;

    @Unowned
    @OneToOne
    private User user;

    @Unowned
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attribute> attributes = new HashSet<Attribute>();

    public Feedback() {
        super();
    }

    /**
     * @param campaign
     * @param user
     * @param attributes
     */
    public Feedback(final Campaign campaign, final User user, final Set<Attribute> attributes) {
        this();
        this.campaign = campaign;
        this.campaign.addFeedback(this);
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    @JsonIgnore
    public Campaign getCampaign() {
        return campaign;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(final Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void addAttribute(final String key, final String value) {
        addAttribute(new Attribute(key, value));
    }

    public <T> T as(final String key, Class<T> type) {
        return getValue(key, type, null);
    }

    public <T> T getValue(final String key, Class<T> type, Object defaultValue) {
        final Optional<Attribute> attribute = tryFind(this.attributes, new Predicate<Attribute>() {
            @Override
            public boolean apply(Attribute attribute) {
                return attribute.getKey().equals(key);
            }
        });
        if (attribute.isPresent()) {
            return (T) converter.convert(attribute.get().getValue(), type);
        }
        return (T) defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (!getId().equals(feedback.getId())) return false;
        if (!campaign.equals(feedback.campaign)) return false;
        if (!created.equals(feedback.created)) return false;
        if (!user.equals(feedback.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = campaign.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

}
