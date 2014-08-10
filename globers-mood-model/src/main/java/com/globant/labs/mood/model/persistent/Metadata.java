package com.globant.labs.mood.model.persistent;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Metadata extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7608867209235862065L;

    @Basic
    private String key;

    @Basic
    private String name;

    @Basic
    private String type;

    public Metadata() {
        super();
    }

    /**
     *
     * @param key
     */
    public Metadata(final String key) {
        this(key, key);
    }

    /**
     *
     * @param key
     * @param name
     */
    public Metadata(final String key, final String name) {
        this(key, name, String.class.toString());
    }

    /**
     *
     * @param key
     * @param name
     * @param type
     */
    public Metadata(final String key, final String name, final String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metadata metadata = (Metadata) o;

        if (!getId().equals(metadata.getId())) return false;
        if (!key.equals(metadata.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

}
