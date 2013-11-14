package com.globant.labs.mood.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.model.setup.CampaignRelation;
import com.globant.labs.mood.model.setup.ImportInformation;
import com.globant.labs.mood.model.setup.Relation;
import com.globant.labs.mood.repository.data.*;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.ImporterService;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.support.StringSupport.on;


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class ImporterServiceImpl extends AbstractService implements ImporterService {

    private static final Logger logger = LoggerFactory.getLogger(ImporterServiceImpl.class);

    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private ProjectRepository projectRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private TemplateRepository templateRepository;
    @Inject
    private CampaignRepository campaignRepository;
    @Inject
    private PreferenceRepository preferenceRepository;

    @Inject
    private ObjectMapper objectMapper;

    private List<Customer> storedCustomers = new ArrayList<Customer>();
    private List<User> storedUsers = new ArrayList<User>();
    private List<Project> storedProjects = new ArrayList<Project>();
    private List<Template> storedTemplates = new ArrayList<Template>();
    private List<Campaign> storedCampaigns = new ArrayList<Campaign>();
    private List<Preference> storedPreferences = new ArrayList<Preference>();

    private Map<String, Object> results = new HashMap<String, Object>();

    /**
     * @param importInformation
     * @return
     */
    @Override
    public Map<String, Object> importData(final ImportInformation importInformation) {
        final StopWatch stopWatch = new StopWatch();

        // == Preferences
        stopWatch.start();
        storePreferences(importInformation);
        stopWatch.stop();
        results.put("preferences", storedPreferences.size());
        results.put("preferences.elapsed", stopWatch.getTotalTimeMillis());

        // == Customer
        stopWatch.start();
        storeCustomers(importInformation);
        stopWatch.stop();
        results.put("customers", storedCustomers.size());
        results.put("customers.elapsed", stopWatch.getTotalTimeMillis());

        // == Users
        stopWatch.start();
        storeUsers(importInformation);
        stopWatch.stop();
        results.put("users", storedUsers.size());
        results.put("users.elapsed", stopWatch.getTotalTimeMillis());

        // == Templates
        stopWatch.start();
        storeTemplates(importInformation);
        stopWatch.stop();
        results.put("templates", storedTemplates.size());
        results.put("templates.elapsed", stopWatch.getTotalTimeMillis());

        // == Campaigns
        stopWatch.start();
        storeCampaigns(importInformation);
        stopWatch.stop();
        results.put("campaigns", storedCampaigns.size());
        results.put("campaigns.elapsed", stopWatch.getTotalTimeMillis());

        // == Projects
        stopWatch.start();
        storeProjectRelations(importInformation);
        stopWatch.stop();
        results.put("projects", storedProjects.size());
        results.put("projects.elapsed", stopWatch.getTotalTimeMillis());

        // == Campaigns
        stopWatch.start();
        storeCampaignRelations(importInformation);
        stopWatch.stop();
        results.put("campaigns", storedCampaigns.size());
        results.put("campaigns.elapsed", stopWatch.getTotalTimeMillis());

        return results;
    }

    private void storeCampaignRelations(ImportInformation importInformation) {
        final List<CampaignRelation> campaignRelations = importInformation.getCampaignRelations();
        for (final CampaignRelation campaignRelation : campaignRelations) {
            final Campaign campaign = storedCampaigns.get(campaignRelation.getCampaign());
            campaign.setTemplate(storedTemplates.get(campaignRelation.getTemplate()));

            List<Integer> usersIndex = campaignRelation.getUsers();
            for (int userIndex : usersIndex) {
                final User target = storedUsers.get(userIndex);
                campaign.addTarget(target);
            }
        }
    }

    private void storeProjectRelations(ImportInformation importInformation) {
        final List<Relation> relations = importInformation.getRelations();
        for (final Relation relation : relations) {
            int projectIndex = relation.getProject();
            int customerIndex = relation.getCustomer();

            final Customer storedCustomer = storedCustomers.get(customerIndex);
            if (storedCustomer == null) {
                throw new BusinessException("project must have a customer associated.", EXPECTATION_FAILED);
            }

            final Project mappedProject = importInformation.getProjects().get(projectIndex);
            final String name = mappedProject.getName();

            final Project projectPrototype = new Project(name, storedCustomer);

            final List<Integer> userIndexes = relation.getUsers();
            for (final Integer currentUserIndex : userIndexes) {
                final User user = storedUsers.get(currentUserIndex);
                projectPrototype.assign(user);
            }
            storedProjects.add(projectRepository.saveAndFlush(projectPrototype));
        }
    }

    private void storePreferences(ImportInformation importInformation) {
        final List<Preference> preferences = importInformation.getPreferences();
        for (final Preference preference : preferences) {
            storedPreferences.add(preferenceRepository.saveAndFlush(preference));
        }
    }

    private void storeCampaigns(ImportInformation importInformation) {
        final List<Campaign> campaigns = importInformation.getCampaigns();
        for (final Campaign campaign : campaigns) {
            storedCampaigns.add(campaignRepository.saveAndFlush(campaign));
        }
    }

    private void storeTemplates(ImportInformation importInformation) {
        final List<Template> templates = importInformation.getTemplates();
        for (final Template template : templates) {
            storedTemplates.add(templateRepository.saveAndFlush(template));
        }
    }

    private void storeUsers(ImportInformation importInformation) {
        final List<User> users = importInformation.getUsers();
        for (final User user : users) {
            storedUsers.add(userRepository.saveAndFlush(user));
        }
    }

    private void storeCustomers(ImportInformation importInformation) {
        final List<Customer> customers = importInformation.getCustomers();
        for (final Customer customer : customers) {
            storedCustomers.add(customerRepository.saveAndFlush(customer));
        }
    }

    /**
     * @param inputStream
     * @return
     */
    @Override
    public Map<String, Object> importData(final InputStream inputStream) {
        final byte[] bytes;
        try {
            bytes = ByteStreams.toByteArray(inputStream);
            return importData(objectMapper.readValue(new String(bytes, Charsets.UTF_8), ImportInformation.class));

        } catch (IOException e) {
            logger.debug("An exception occurred trying to de-serialize Import Information.");
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Date());
    }
}
