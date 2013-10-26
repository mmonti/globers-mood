package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.appengine.api.datastore.Key;
import org.datanucleus.api.jpa.annotations.Extension;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity implements Identity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private transient Long id;

    public BaseEntity() {}

    @JsonIgnore
    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
        this.setId(key.getId());
    }

    public Long getId() {
        if (this.id == null) {
            setId(this.key.getId());
            return this.id;
        }
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
