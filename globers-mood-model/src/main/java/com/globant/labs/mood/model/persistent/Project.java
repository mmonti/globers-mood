package com.globant.labs.mood.model.persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Project extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7601336882893266721L;

    @Basic
    private String name;

    @Unowned
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

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

    public Customer getCustomer() {
        return customer;
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
