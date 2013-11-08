package com.globant.labs.mood.model.setup;

import com.globant.labs.mood.model.persistent.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class ImportInformation implements Serializable {

    private static final long serialVersionUID = 361412404144632042L;

    private List<Project> projects;
    private List<Customer> customers;
    private List<User> users;
    private List<Campaign> campaigns;
    private List<Template> templates;
    private List<Relation> relations;
    private List<Preference> preferences;
    private List<CampaignRelation> campaignRelations;

    /**
     *
     */
    public ImportInformation() {
        this.projects = new ArrayList<Project>();
        this.customers = new ArrayList<Customer>();
        this.users = new ArrayList<User>();
        this.campaigns = new ArrayList<Campaign>();
        this.templates = new ArrayList<Template>();
        this.relations = new ArrayList<Relation>();
        this.preferences = new ArrayList<Preference>();
        this.campaignRelations = new ArrayList<CampaignRelation>();
    }

    /**
     *
     */
    public ImportInformation(final List<Project> projects, final List<Customer> customers, final List<User> users, final List<Campaign> campaigns, final List<Template> templates, final List<Relation> relations, final List<CampaignRelation> campaignRelations, final List<Preference> preferences) {
        this.projects = projects;
        this.customers = customers;
        this.users = users;
        this.campaigns = campaigns;
        this.templates = templates;
        this.relations = relations;
        this.preferences = preferences;
        this.campaignRelations = campaignRelations;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<CampaignRelation> getCampaignRelations() {
        return campaignRelations;
    }

    public void setCampaignRelations(List<CampaignRelation> campaignRelations) {
        this.campaignRelations = campaignRelations;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }
}