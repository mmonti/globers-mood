package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.service.*;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.globant.labs.mood.support.TransactionSupport;
import com.google.appengine.api.datastore.Blob;
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
import java.util.List;
import java.util.Set;


/**
* @author mauro.monti (monti.mauro@gmail.com)
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class, loader=AnnotationConfigContextLoader.class)
public class CampaignServiceImplTest extends TransactionSupport {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private CampaignService campaignService;
    @Inject
    private ProjectService projectService;
    @Inject
    private CustomerService customerService;
    @Inject
    private UserService userService;
    @Inject
    private TemplateService templateService;
    @Inject
    private FeedbackService feedbackService;
    @Inject
    private PreferenceService preferenceService;
    @Inject
    private TokenGenerator tokenGenerator;

    Campaign campaign = null;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();

        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        project.assign(storedUser);

        final Project storedProject = projectService.store(project);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        final Template storedTemplate = templateService.store(template);

        campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);

        final Preference senderAlias = new Preference(PreferenceKey.SENDER_ALIAS, "alias");
        final Preference senderMail = new Preference(PreferenceKey.SENDER_MAIL, "mail");
        final Preference mailSubject = new Preference(PreferenceKey.MAIL_SUBJECT, "subject");

        preferenceService.store(senderAlias);
        preferenceService.store(senderMail);
        preferenceService.store(mailSubject);
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testAddCampaign() throws Exception {
        final Campaign storedCampaign = campaignService.store(campaign);

        Assert.notNull(storedCampaign);
        Assert.notNull(storedCampaign.getId());
        Assert.isTrue(storedCampaign.getName().equals(campaign.getName()));
        Assert.notEmpty(storedCampaign.getTargets());
        Assert.notNull(storedCampaign.getTemplate());
    }

    @Test
    public void testCampaigns() throws Exception {
        final Campaign storedCampaign = campaignService.store(campaign);
        final Set<Campaign> campaigns = campaignService.campaigns();
        Assert.notNull(campaigns);
        Assert.notEmpty(campaigns);
    }

    @Test
    public void testCampaignById() throws Exception {
        final Campaign storedCampaign = campaignService.store(campaign);
        final Campaign result = campaignService.campaign(storedCampaign.getId());
        Assert.notNull(result);
        Assert.isTrue(storedCampaign.getName().equals(result.getName()));
        Assert.notEmpty(storedCampaign.getTargets());
        Assert.notNull(result.getTemplate());
    }

    @Test
    public void testMostActiveCampaigns() throws Exception {
        final User user1 = new User("user1", "user1@globant.com");
        final User storedUser1 = userService.store(user1);
        final User user2 = new User("user2", "user2@globant.com");
        final User storedUser2 = userService.store(user2);
        final User user3 = new User("user3", "user3@globant.com");
        final User storedUser3 = userService.store(user3);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project1 = new Project("Project1", storedCustomer);
        project1.assign(storedUser1);
        project1.assign(storedUser2);

        final Project project2 = new Project("Project2", storedCustomer);
        project2.assign(storedUser3);

        final Project storedProject1 = projectService.store(project1);
        final Project storedProject2 = projectService.store(project2);

        final Campaign campaign1 = new Campaign("Campaign1");
        campaign1.addTarget(storedUser1);

        final Campaign campaign2 = new Campaign("Campaign2");
        campaign2.addTarget(storedUser2);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);
        campaign1.setTemplate(storedTemplate);
        campaign2.setTemplate(storedTemplate);

        final Campaign storedCampaign1 = campaignService.store(campaign1);
        final Campaign storedCampaign2 = campaignService.store(campaign2);

        final String token1 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign1, storedUser1);
        final String token2 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign2, storedUser2);
        final String token3 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign2, storedUser3);

        final Feedback storedFeedback1 = feedbackService.store(storedCampaign1.getId(), storedUser1.getEmail(), token1, Mood.NEUTRAL, Mood.NEUTRAL, "Comments");
        final Feedback storedFeedback2 = feedbackService.store(storedCampaign2.getId(), storedUser2.getEmail(), token2, Mood.NEUTRAL, Mood.NEUTRAL, "Comments");
        final Feedback storedFeedback3 = feedbackService.store(storedCampaign2.getId(), storedUser3.getEmail(), token3, Mood.NEUTRAL, Mood.NEUTRAL, "Comments");

        Assert.notNull(storedFeedback1);
        Assert.notNull(storedFeedback2);
        Assert.notNull(storedFeedback3);

        Assert.isTrue(storedFeedback1.getCampaign().getFeedbackNumber()==1);
        Assert.isTrue(storedFeedback2.getCampaign().getFeedbackNumber()==2);

        final List<Campaign> campaigns = campaignService.mostActive();

        Assert.notEmpty(campaigns);
        Assert.isTrue(campaigns.size()==2);
        Assert.isTrue(campaigns.get(0).getFeedbackNumber()==2);
        Assert.isTrue(campaigns.get(1).getFeedbackNumber()==1);
    }

    @Test
    public void testStartCampaign() throws Exception {
        final Campaign storedCampaign = campaignService.store(campaign);
        campaignService.start(storedCampaign.getId());
    }
}
