package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.service.CampaignService;
import com.globant.labs.mood.service.FeedbackService;
import com.globant.labs.mood.service.TemplateService;
import com.globant.labs.mood.service.UserService;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.globant.labs.mood.support.jersey.FeedbackContent;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.labs.repackaged.com.google.common.collect.Sets;
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
@ContextConfiguration(classes = RootConfig.class, loader = AnnotationConfigContextLoader.class)
public class FeedbackServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private FeedbackService feedbackService;
    @Inject
    private UserService userService;
    @Inject
    private CampaignService campaignService;
    @Inject
    private TemplateService templateService;

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

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);

        final Campaign storedCampaign = campaignService.store(campaign);

        storedCampaign.start().waitForFeedback();
        campaignService.store(storedCampaign);

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);
        final FeedbackContent feedbackContainer = new FeedbackContent(storedCampaign.getId(), storedUser.getEmail(), token, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback = feedbackService.store(feedbackContainer);

        final Set<Feedback> storedFeedbackOfCampaign = feedbackService.feedbackOfCampaign(storedFeedback.getCampaign().getId());
        Assert.notNull(storedFeedbackOfCampaign);
        Assert.notEmpty(storedFeedbackOfCampaign);
    }

    @Test
    public void testFeedbackOfUser() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);

        final Campaign storedCampaign = campaignService.store(campaign);

        storedCampaign.start().waitForFeedback();
        campaignService.store(storedCampaign);

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);

//        final Feedback storedFeedback = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "This is my Comment");
        final FeedbackContent feedbackContainer = new FeedbackContent(storedCampaign.getId(), storedUser.getEmail(), token, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback = feedbackService.store(feedbackContainer);

        final Set<Feedback> storedFeedbackOfCampaign = feedbackService.feedbackOfUser(storedUser.getId());
        Assert.notNull(storedFeedbackOfCampaign);
        Assert.notEmpty(storedFeedbackOfCampaign);
    }

    @Test
    public void testFeedbackOfUserCampaign() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);

        final Campaign storedCampaign = campaignService.store(campaign);

        storedCampaign.start().waitForFeedback();
        campaignService.store(storedCampaign);

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);

        final FeedbackContent feedbackContainer = new FeedbackContent(storedCampaign.getId(), storedUser.getEmail(), token, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback = feedbackService.store(feedbackContainer);

        final Set<Feedback> storedFeedbackOfCampaign = feedbackService.feedbackOfUserCampaign(storedCampaign.getId(), storedUser.getId());
        Assert.notNull(storedFeedbackOfCampaign);
        Assert.notEmpty(storedFeedbackOfCampaign);
    }

    @Test
    public void addFeedbackTest() {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);
        final Campaign storedCampaign = campaignService.store(campaign);

        storedCampaign.start().waitForFeedback();
        campaignService.store(storedCampaign);

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);
        final FeedbackContent feedbackContainer = new FeedbackContent(storedCampaign.getId(), storedUser.getEmail(), token, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback = feedbackService.store(feedbackContainer);


        Assert.notNull(storedFeedback);
        Assert.notNull(storedFeedback.getId());
        Assert.notNull(storedFeedback.getAttributes());
    }

    @Test(expected = BusinessException.class)
    public void testFeedbackAlreadySubmitted() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);

        final Campaign campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);
        campaign.start().waitForFeedback();

        final Campaign storedCampaign = campaignService.store(campaign);
        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign, storedUser);

        final FeedbackContent feedbackContainer = new FeedbackContent(storedCampaign.getId(), storedUser.getEmail(), token, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedbackFirst = feedbackService.store(feedbackContainer);
        final Feedback storedFeedbackSecond = feedbackService.store(feedbackContainer);

        Assert.notNull(storedFeedbackFirst);
        Assert.notNull(storedFeedbackSecond);
    }
}
