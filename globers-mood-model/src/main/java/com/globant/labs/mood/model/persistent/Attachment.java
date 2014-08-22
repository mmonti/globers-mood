package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.globant.labs.mood.support.jackson.Base64BlobDeserializer;
import com.google.appengine.api.datastore.Blob;
import com.google.common.base.Preconditions;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Attachment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7841829072159026119L;

    private static final String LESS_THAN = "<";
    private static final String GREATER_THAN = ">";
    private static final String DOT = ".";

    @Basic
    private String filename;

    @Enumerated(EnumType.STRING)
    private MimeType mimeType;

    @Basic
    private Blob content;

    /**
     *
     */
    public Attachment() {
        super();
    }

    /**
     * @param filename
     */
    public Attachment(final String filename) {
        this(filename, MimeType.getFromExtension(extension(filename)));
    }

    /**
     * @param filename
     * @param mimeType
     */
    public Attachment(final String filename, final MimeType mimeType) {
        Preconditions.checkNotNull(filename, "filename is null");
        Preconditions.checkNotNull(mimeType, "mimeType is null");

        this.filename = filename;
        this.mimeType = mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilenameID() {
        final StringBuffer buffer = new StringBuffer(filename);
        return buffer.substring(0, buffer.indexOf(DOT)).toString();
    }

    public String getContentID() {
        return new StringBuffer().insert(0, LESS_THAN).append(getFilenameID()).append(GREATER_THAN).toString();
    }

    public void setFilename(final String filename) {
        this.filename = filename;
        this.mimeType = MimeType.getFromFile(filename);
    }

    @JsonIgnore
    public Blob getContent() {
        return content;
    }

    @JsonDeserialize(using = Base64BlobDeserializer.class)
    public void setContent(final Blob file) {
        this.content = file;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    private static String extension(final String filename) {
        final StringBuffer buffer = new StringBuffer(filename);
        return buffer.substring(buffer.indexOf(DOT) + 1).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment attachment = (Attachment) o;

        if (!getId().equals(attachment.getId())) return false;
        if (!created.equals(attachment.created)) return false;
        if (!filename.equals(attachment.filename)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (getId() != null) {
            result = getId().hashCode() * 31;
        }
        if (getFilename() != null) {
            result += getContent().hashCode() * 64;
        }
        return result + created.hashCode();
    }

}
