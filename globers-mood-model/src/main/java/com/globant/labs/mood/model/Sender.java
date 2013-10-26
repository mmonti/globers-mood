package com.globant.labs.mood.model;

import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class Sender implements Serializable {

    private static final long serialVersionUID = -6361519657742615375L;

    private String alias;
    private String mail;

    public Sender(final String alias, final String mail) {
        this.alias = alias;
        this.mail = mail;
    }

    public String getAlias() {
        return alias;
    }

    public String getMail() {
        return mail;
    }

}
