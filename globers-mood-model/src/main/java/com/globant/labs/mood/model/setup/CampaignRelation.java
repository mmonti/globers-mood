package com.globant.labs.mood.model.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class CampaignRelation implements Serializable {

    private static final long serialVersionUID = 8626564472896907364L;

    private List<Integer> users;
    private int template;
    private int campaign;

    public CampaignRelation() {
        this.users = new ArrayList<Integer>();
    }

    public CampaignRelation(final int campaign, final int template, final List<Integer> users) {
        this.campaign = campaign;
        this.users = users;
        this.template = template;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public int getCampaign() {
        return campaign;
    }

    public void setCampaign(int campaign) {
        this.campaign = campaign;
    }
}
