package com.globant.labs.mood.model.persistent;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class ValueMapping extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7608867209235862065L;

    @Basic
    private String key;

    @Basic
    private String value;

    public ValueMapping() {
        super();
    }

    /**
     * @param key
     */
    public ValueMapping(final String key) {
        this(key, null);
    }

    /**
     * @param key
     * @param value
     */
    public ValueMapping(final String key, final String value) {
        this();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueMapping attribute = (ValueMapping) o;
        if (getId() == null) {
            return false;
        }
        if (!getId().equals(attribute.getId())) return false;
        if (!key.equals(attribute.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result;
        return result + created.hashCode();
    }

}
