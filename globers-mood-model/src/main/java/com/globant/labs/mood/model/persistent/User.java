package com.globant.labs.mood.model.persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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

    @Unowned
    @OneToOne(cascade = CascadeType.ALL)
    private Project project;

    public User() {
        super();
    }

    /**
     * @param name
     * @param email
     */
    public User(final String name, final String email) {
        this();
        this.name = name;
        this.email = email;
    }

    /**
     * @param name
     * @param email
     * @param project
     */
    public User(final String name, final String email, final Project project) {
        this(name, email);
        setProject(project);
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!getId().equals(user.getId())) return false;
        if (!email.equals(user.email)) return false;
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