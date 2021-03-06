package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Basic;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@javax.persistence.Entity
public class Preference extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6467812617796865127L;

    @Basic
    private String preferenceKey;
    @Basic
    private String preferenceValue;
    @Basic
    private String namespace;
    @Basic
    private boolean readOnly = false;

    public Preference() {
        super();
    }

    /**
     * @param key
     */
    public Preference(final String key) {
        this(key, null);
    }

    /**
     * @param key
     */
    public Preference(final PreferenceKey key) {
        this(key.getValue(), null);
    }

    /**
     * @param key
     * @param value
     */
    public Preference(final String key, final String value) {
        this(key, value, null);
    }

    /**
     * @param namespace
     * @param key
     * @param value
     */
    public Preference(final String key, final String value, final String namespace) {
        this();
        this.namespace = namespace;
        this.preferenceKey = key;
        this.preferenceValue = value;
    }

    /**
     * @param key
     * @param value
     * @param namespace
     * @param readOnly
     */
    public Preference(final String key, final String value, final String namespace, final boolean readOnly) {
        this(key, value, namespace);
        this.setReadOnly(readOnly);
    }

    /**
     * @param key
     * @param value
     */
    public Preference(final PreferenceKey key, final String value) {
        this(key, value, null);
    }

    /**
     * @param namespace
     * @param key
     * @param value
     */
    public Preference(final PreferenceKey key, final String value, final String namespace) {
        this(key.getValue(), value, namespace);
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }

    @JsonIgnore
    public PreferenceKey getPreferenceKeyEnum() {
        return PreferenceKey.valueOf(getPreferenceKey());
    }

    public String getPreferenceValue() {
        return preferenceValue;
    }

    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preference that = (Preference) o;

        if (!preferenceKey.equals(that.preferenceKey)) return false;
        if (preferenceValue != null ? !preferenceValue.equals(that.preferenceValue) : that.preferenceValue != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = preferenceKey.hashCode();
        result = 31 * result + (preferenceValue != null ? preferenceValue.hashCode() : 0);
        return result;
    }
}
