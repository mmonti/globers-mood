package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.globant.labs.mood.support.jackson.BlobDeserializer;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Iterables.tryFind;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Template extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7841829072159026119L;

    @Basic
    private String name;

    @Basic
    private String description;

    @Basic
    private String filename;

    @Unowned
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TemplateMetadata metadata;

    @Basic
    private Blob content;

    @Unowned
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Attachment> attachments = new HashSet<Attachment>();

    /**
     *
     */
    public Template() {
        super();
    }

    /**
     * @param name
     */
    public Template(final String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public TemplateMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(final TemplateMetadata metadata) {
        this.metadata = metadata;
    }

    @JsonIgnore
    public Blob getContent() {
        return content;
    }

    public String getTemplateContent() {
        if (content != null) {
            return new String(content.getBytes(), Charsets.UTF_8);
        }
        return null;
    }

    @JsonDeserialize(using = BlobDeserializer.class)
    public void setContent(final Blob content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void addAttachment(final Attachment attachment) {
        if (!attachments.contains(attachment)) {
            attachments.add(attachment);
        }
    }

    public Attachment getAttachment(final String filename) {
        final Optional<Attachment> attachment = tryFind(attachments, new Predicate<Attachment>() {
            @Override
            public boolean apply(Attachment attachment) {
                return filename.equals(attachment.getFilename());
            }
        });
        return attachment.orNull();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (!getId().equals(template.getId())) return false;
        if (!created.equals(template.created)) return false;
        if (!name.equals(template.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (getId() != null) {
            result = getId().hashCode() * 31;
        }
        if (getName() != null) {
            result += getName().hashCode() * 64;
        }
        return result + created.hashCode();
    }

}
