package com.globant.labs.mood.model.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class ProjectRelation implements Serializable {

    private static final long serialVersionUID = -4838989147865815040L;

    private List<Integer> users;
    private int project;
    private int customer;

    /**
     *
     */
    public ProjectRelation() {
        this.users = new ArrayList<Integer>();
    }

    /**
     * @param users
     * @param project
     * @param customer
     */
    public ProjectRelation(final List<Integer> users, final int project, final int customer) {
        this.users = users;
        this.project = project;
        this.customer = customer;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }
}
