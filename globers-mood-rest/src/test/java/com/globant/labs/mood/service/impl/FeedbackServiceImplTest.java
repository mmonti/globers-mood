package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.exception.ServiceException;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.service.*;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.Set;

/**
* @author mauro.monti (monti.mauro@gmail.com)
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class, loader=AnnotationConfigContextLoader.class)
public class FeedbackServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private FeedbackService feedbackService;
    @Inject
    private UserService userService;
    @Inject
    private CampaignService campaignService;
    @Inject
    private ProjectService projectService;
    @Inject
    private CustomerService customerService;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private TokenGenerator tokenGenerator;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testFeedbackOfCampaign() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        project.assign(storedUser);

        final Project storedProject = projectService.store(project);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);

        final Campaign storedCampaign = campaignService.store(campaign);

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);
        final Feedback storedFeedback = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "This is my Comment");

        final Set<Feedback> storedFeedbackOfCampaign = feedbackService.feedbackOfCampaign(storedFeedback.getCampaign().getId());
        Assert.notNull(storedFeedbackOfCampaign);
        Assert.notEmpty(storedFeedbackOfCampaign);
    }

    @Test
    public void testFeedbackOfUser() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        project.assign(storedUser);

        final Project storedProject = projectService.store(project);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);

        final Campaign storedCampaign = campaignService.store(campaign);
        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);

        final Feedback storedFeedback = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "This is my Comment");

        final Set<Feedback> storedFeedbackOfCampaign = feedbackService.feedbackOfUser(storedCampaign.getId(), storedUser.getId());
        Assert.notNull(storedFeedbackOfCampaign);
        Assert.notEmpty(storedFeedbackOfCampaign);
    }

    @Test
    public void addFeedbackTest() {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        project.assign(storedUser);

        final Project storedProject = projectService.store(project);

        final Campaign campaign = new Campaign("Campaign");
//        campaign.addProject(storedProject);
        campaign.addTarget(storedUser);

        final Campaign storedCampaign = campaignService.store(campaign);
        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);

        final Feedback storedFeedback = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "");
        Assert.notNull(storedFeedback);
        Assert.notNull(storedFeedback.getId());
        Assert.isTrue(storedFeedback.getGloberMood().equals(Mood.NEUTRAL));
        Assert.isTrue(storedFeedback.getClientMood().equals(Mood.NEUTRAL));
    }

    private Feedback getFeedback(int i, Integer c) {
        final User user = new User("Mauro Monti"+i, "mauro.monti@globant.com"+i);
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer"+i);
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project"+i, storedCustomer);
        project.assign(storedUser);

        final Project storedProject = projectService.store(project);

        Campaign campaign = null;
        if (c == null) {
            campaign = new Campaign("Campaign"+i);
            campaign.addTarget(storedUser);
            campaign = campaignService.store(campaign);

        } else {
            campaign = campaignService.campaign(c.longValue());
        }

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(campaign, storedUser);
        final Feedback storedFeedback = feedbackService.store(campaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "");
        Assert.notNull(storedFeedback);
        Assert.notNull(storedFeedback.getId());
        Assert.isTrue(storedFeedback.getGloberMood().equals(Mood.NEUTRAL));
        Assert.isTrue(storedFeedback.getClientMood().equals(Mood.NEUTRAL));

        return storedFeedback;
    }

    @Test(expected = ServiceException.class)
    public void testFeedbackAlreadySubmitted() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        project.assign(storedUser);

        final Project storedProject = projectService.store(project);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);

        final Campaign storedCampaign = campaignService.store(campaign);
        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);
        final Feedback storedFeedbackFirst = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "This is my comment");
        final Feedback storedFeedbackSecond = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "This is my comment");

        Assert.notNull(storedFeedbackFirst);
        Assert.notNull(storedFeedbackSecond);
    }
}
