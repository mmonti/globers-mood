package com.globant.labs.mood.model.mail;

import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailSettings implements Serializable {

    private static final long serialVersionUID = -6361519657742615375L;

    private String alias;
    private String mail;
    private String subject;

    /**
     * @param alias
     * @param mail
     * @param subject
     */
    public MailSettings(final String alias, final String mail, final String subject) {
        this.alias = alias;
        this.mail = mail;
        this.subject = subject;
    }

    public String getAlias() {
        return alias;
    }

    public String getMail() {
        return mail;
    }

    public String getSubject() {
        return subject;
    }
}
