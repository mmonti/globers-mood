package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private ElementType elementType;

    @Basic
    private String valueType;

    @Unowned
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValueMapping> values = new ArrayList<ValueMapping>();

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
     * @param key
     * @param name
     * @param description
     */
    public ElementMetadata(final String key, final String name, final String description) {
        this(key, name, description, ElementType.CHOICE);
    }

    /**
     * @param key
     * @param name
     * @param description
     * @param elementType
     */
    public ElementMetadata(final String key, final String name, final String description, final ElementType elementType) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.elementType = elementType;
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

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(final ElementType type) {
        this.elementType = type;
    }

    @JsonIgnore
    public List<String> getValues() {
        return Lists.newArrayList(Iterables.transform(values, new Function<ValueMapping, String>() {
            @Override
            public String apply(final ValueMapping valueMapping) {
                return valueMapping.getKey();
            }
        }));
    }

    public List<ValueMapping> getValueMappings() {
        return this.values;
    }

    public void addValue(final ValueMapping valueMapping) {
        this.values.add(valueMapping);
    }

    public void addValue(final String key, final String value) {
        this.values.add(new ValueMapping(key, value));
    }

    public void addValues(final List<String> values) {
        for (final String currentValue : values) {
            addValue(currentValue, currentValue);
        }
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
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

        if (getId() == null) {
            return false;
        }
        if (!getId().equals(metadata.getId())) return false;
        if (!key.equals(metadata.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (getId() != null) {
            result = getId().hashCode() * 31;
        }
        return result + created.hashCode();
    }

}
