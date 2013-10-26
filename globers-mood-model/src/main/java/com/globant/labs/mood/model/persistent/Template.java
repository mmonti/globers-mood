package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.globant.labs.mood.jackson.TemplateFileDeserializer;
import com.google.appengine.api.datastore.Blob;
import com.google.common.base.Charsets;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Template extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7841829072159026119L;

    @Basic
    private String name;
    @Basic
    private Blob file;

    @Temporal(TemporalType.DATE)
    private Date created;

    public Template() {
        this.created = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Blob getFile() {
        return file;
    }

    public String getTemplate() {
        if (file != null) {
            return new String(file.getBytes(), Charsets.UTF_8);
        }
        return null;
    }

    @JsonDeserialize(using = TemplateFileDeserializer.class)
    public void setFile(Blob file) {
        this.file = file;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (!created.equals(template.created)) return false;
        if (!name.equals(template.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

}
