package com.globant.labs.mood.model.persistent;

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
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5355798826927099635L;

    @Basic
    private String name;

    @Basic
    private String email;
    @Basic
    private boolean assigned;

    @Temporal(TemporalType.DATE)
    private Date created;

    public User() {
        this.created = new Date();
    }

    /**
     * @param name
     * @param email
     */
    public User(final String name, final String email) {
        this();
        this.name = name;
        this.email = email;
        this.assigned = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void release() {
        this.assigned = false;
    }

    public void assign() {
        this.assigned = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;
        if (!getKey().equals(user.getKey())) return false;
        if (!name.equals(user.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

}