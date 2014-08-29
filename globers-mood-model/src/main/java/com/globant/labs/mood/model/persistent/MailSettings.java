package com.globant.labs.mood.model.persistent;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class MailSettings extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6361519657742615375L;

    @Basic
    private String alias;

    @Basic
    private String mail;

    @Basic
    private String subject;

    public MailSettings() {
        super();
    }

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

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setSubject(String subject) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailSettings that = (MailSettings) o;

        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (mail != null ? !mail.equals(that.mail) : that.mail != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = alias != null ? alias.hashCode() : 0;
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }
}
