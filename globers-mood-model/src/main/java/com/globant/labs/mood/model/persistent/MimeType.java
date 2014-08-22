package com.globant.labs.mood.model.persistent;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mmonti on 8/14/14.
 */
public enum MimeType {

    GIF("image/gif", "gif"),
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpeg", "jpg", "jpe"),

    MS_WORD("application/msword", "doc", "docx"),
    PDF("application/pdf", "pdf"),
    G_ZIP("application/x-gzip", "gzip"),
    ZIP("application/zip", "zip"),
    JSON("application/json", "json"),
    HTML("text/html", "html", "htm"),
    CSV("text/comma-separated-values", "csv"),
    CSS("text/css", "css"),
    TEXT("text/plain", "text", "txt", "asc", "diff", "pot");

    private static final String DOT = ".";

    private String mimeType;
    private List<String> extensions;

    /**
     * @param mimeType
     * @param extensions
     */
    MimeType(final String mimeType, final String... extensions) {
        this.mimeType = mimeType;
        this.extensions = Arrays.asList(extensions);
    }

    /**
     * @return
     */
    public String getType() {
        return this.mimeType;
    }

    /**
     * @return
     */
    private List<String> getExtensions() {
        return extensions;
    }

    /**
     * @param filename
     * @return
     */
    public static MimeType getFromFile(final String filename) {
        final StringBuffer buffer = new StringBuffer(filename);
        return getFromExtension(buffer.substring(buffer.indexOf(DOT) + 1).toString());
    }

    /**
     * @param extension
     * @return
     */
    public static MimeType getFromExtension(final String extension) {
        Preconditions.checkNotNull(extension, "extension is null");

        for (final MimeType mimeType : values()) {
            if (mimeType.getExtensions().contains(extension)) {
                return mimeType;
            }
        }
        return null;
    }
}
