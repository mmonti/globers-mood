package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;
import com.google.appengine.datanucleus.annotations.Unowned;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Project extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7601336882893266721L;

    @Basic
    private String name;

    @Unowned
    @OneToOne
    private Customer customer;

    @Unowned
    @OneToMany
    private Set<User> users = new HashSet<User>();

    @Basic
    private int userNumber = 0;

    public Project() {
        super();
    }

    /**
     * @param name
     * @param customer
     */
    public Project(final String name, final Customer customer) {
        this();
        this.name = name;
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void assign(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        this.users.add(user);
        this.userNumber++;
    }

    public boolean release(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        this.userNumber--;
        return this.users.remove(user);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getUserNumber() {
        return userNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (!getId().equals(project.getId())) return false;
        if (!name.equals(project.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
