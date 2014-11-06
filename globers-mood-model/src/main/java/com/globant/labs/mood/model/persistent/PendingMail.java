package com.globant.labs.mood.model.persistent;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class PendingMail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7608867209235862065L;

    @Basic
    private String campaignName;

    @Basic
    private String userEmail;

    public PendingMail() {
        super();
    }

    /**
     *
     * @param campaignName
     * @param userEmail
     */
    public PendingMail(final String campaignName, final String userEmail) {
        this();
        this.campaignName = campaignName;
        this.userEmail = userEmail;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PendingMail that = (PendingMail) o;

        if (campaignName != null ? !campaignName.equals(that.campaignName) : that.campaignName != null) {
            return false;
        }
        if (userEmail != null ? !userEmail.equals(that.userEmail) : that.userEmail != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = campaignName != null ? campaignName.hashCode() : 0;
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        return result;
    }
}
