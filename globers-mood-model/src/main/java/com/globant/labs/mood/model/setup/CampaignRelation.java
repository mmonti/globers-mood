package com.globant.labs.mood.model.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class CampaignRelation implements Serializable {

    private static final long serialVersionUID = 8626564472896907364L;

    private List<Integer> projects;
    private int template;
    private int campaign;

    public CampaignRelation() {
        this.projects = new ArrayList<Integer>();
    }

    public CampaignRelation(final int campaign, final int template, final List<Integer> projects) {
        this.campaign = campaign;
        this.projects = projects;
        this.template = template;
    }

    public List<Integer> getProjects() {
        return projects;
    }

    public void setProjects(List<Integer> projects) {
        this.projects = projects;
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
