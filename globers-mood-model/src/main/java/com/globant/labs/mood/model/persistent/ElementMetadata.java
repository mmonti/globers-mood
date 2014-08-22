package com.globant.labs.mood.model.persistent;

import com.google.appengine.datanucleus.annotations.Unowned;
import org.w3c.dom.Attr;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class ElementMetadata extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7608867209235862065L;

    @Basic
    private String key;

    @Basic
    private String name;

    @Basic
    private String description;

    @Enumerated(EnumType.STRING)
    private ElementType type;

    @Unowned
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attribute> attributes = new HashSet<Attribute>();

    /**
     *
     */
    public ElementMetadata() {
        super();
    }

    /**
     * @param key
     */
    public ElementMetadata(final String key) {
        this(key, key);
    }

    /**
     *
     * @param key
     * @param attributeType
     */
    public ElementMetadata(final String key, ElementType attributeType) {
        this(key, key, key, attributeType);
    }

    /**
     * @param key
     * @param name
     */
    public ElementMetadata(final String key, final String name) {
        this(key, name, name);
    }

    /**
     *
     * @param key
     * @param name
     * @param description
     */
    public ElementMetadata(final String key, final String name, final String description) {
        this(key, name, description, ElementType.CHOICE);
    }

    /**
     *
     * @param key
     * @param name
     * @param description
     * @param type
     */
    public ElementMetadata(final String key, final String name, final String description, final ElementType type) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(final ElementType type) {
        this.type = type;
    }

    public void addAttribute(final Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void addAttribute(final String key, final String value) {
        this.attributes.add(new Attribute(key, value));
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementMetadata metadata = (ElementMetadata) o;

        if (!getId().equals(metadata.getId())) return false;
        if (!key.equals(metadata.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

}
