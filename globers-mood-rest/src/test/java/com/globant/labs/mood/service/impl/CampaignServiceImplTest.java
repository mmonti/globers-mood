package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.service.*;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.globant.labs.mood.support.TransactionSupport;
import com.globant.labs.mood.support.jersey.FeedbackContent;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.labs.repackaged.com.google.common.collect.Sets;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class, loader = AnnotationConfigContextLoader.class)
public class CampaignServiceImplTest extends TransactionSupport {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private CampaignService campaignService;
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

        final Template template = new Template();
        template.setName("Template");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        final Template storedTemplate = templateService.store(template);

        campaign = new Campaign("Campaign");
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);

        final Preference senderAlias = new Preference(PreferenceKey.MAIL_SENDER_ALIAS, "alias");
        final Preference senderMail = new Preference(PreferenceKey.MAIL_SENDER, "mail");
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
        final Page<Campaign> campaigns = campaignService.campaigns(new PageRequest(0, 100));
        Assert.notNull(campaigns);
        Assert.notEmpty(campaigns.getContent());
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

        final Campaign campaign1 = new Campaign("Campaign1");
        campaign1.addTarget(storedUser1);

        final Campaign campaign2 = new Campaign("Campaign2");
        campaign2.addTarget(storedUser2);
        campaign2.addTarget(storedUser3);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);
        campaign1.setTemplate(storedTemplate);
        campaign2.setTemplate(storedTemplate);

        final Campaign storedCampaign1 = campaignService.store(campaign1);
        final Campaign storedCampaign2 = campaignService.store(campaign2);

        storedCampaign1.start().waitForFeedback();
        storedCampaign2.start().waitForFeedback();

        campaignService.store(storedCampaign1);
        campaignService.store(storedCampaign2);

        final String token1 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign1, storedUser1);
        final String token2 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign2, storedUser2);
        final String token3 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign2, storedUser3);

        final FeedbackContent feedbackContainer1 = new FeedbackContent(storedCampaign1.getId(), storedUser1.getEmail(), token1, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback1 = feedbackService.store(feedbackContainer1);

        final FeedbackContent feedbackContainer2 = new FeedbackContent(storedCampaign2.getId(), storedUser2.getEmail(), token2, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback2 = feedbackService.store(feedbackContainer2);

        final FeedbackContent feedbackContainer3 = new FeedbackContent(storedCampaign2.getId(), storedUser3.getEmail(), token3, Sets.newHashSet(new Attribute("key", "value")));
        final Feedback storedFeedback3 = feedbackService.store(feedbackContainer3);

        Assert.notNull(storedFeedback1);
        Assert.notNull(storedFeedback2);
        Assert.notNull(storedFeedback3);

        Assert.isTrue(storedFeedback1.getCampaign().getFeedbacks().size() == 1);
        Assert.isTrue(storedFeedback2.getCampaign().getFeedbacks().size() == 2);

        final List<Campaign> campaigns = campaignService.mostActive();

        Assert.notEmpty(campaigns);
        Assert.isTrue(campaigns.size() == 2);
        Assert.isTrue(campaigns.get(0).getFeedbacks().size() == 2);
        Assert.isTrue(campaigns.get(1).getFeedbacks().size() == 1);
    }

    @Test
    public void testStartCampaign() throws Exception {
        final Campaign storedCampaign = campaignService.store(campaign);
        campaignService.start(storedCampaign.getId());
    }

    @Test
    public void testCampaignCollect() throws Exception {
        final User user1 = new User("user1", "user1@globant.com");
        final User storedUser1 = userService.store(user1);
        final User user2 = new User("user2", "user2@globant.com");
        final User storedUser2 = userService.store(user2);

        final Campaign campaign1 = new Campaign("Campaign1");
        campaign1.addTarget(storedUser1);
        campaign1.addTarget(storedUser2);

        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = templateService.store(template);
        campaign1.setTemplate(storedTemplate);

        final Campaign storedCampaign1 = campaignService.store(campaign1);

        storedCampaign1.start().waitForFeedback();

        campaignService.store(storedCampaign1);

        final String token1 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign1, storedUser1);
        final String token2 = ((UserTokenGenerator) tokenGenerator).getToken(storedCampaign1, storedUser2);

        final FeedbackContent feedbackContainer1 = new FeedbackContent(storedCampaign1.getId(), storedUser1.getEmail(), token1, Sets.newHashSet(new Attribute("attribute-1", "5"), new Attribute("attribute-2", "2")));
        final Feedback storedFeedback1 = feedbackService.store(feedbackContainer1);

        final FeedbackContent feedbackContainer2 = new FeedbackContent(storedCampaign1.getId(), storedUser2.getEmail(), token2, Sets.newHashSet(new Attribute("attribute-1", "4"),new Attribute("attribute-2", "6")));
        final Feedback storedFeedback2 = feedbackService.store(feedbackContainer2);

        Assert.notNull(storedFeedback1);
        Assert.notNull(storedFeedback2);

        Assert.isTrue(storedFeedback1.getCampaign().getFeedbacks().size() == 2);

        final Campaign campaign = campaignService.campaign(storedCampaign1.getId());
        Assert.notNull(campaign);

        List<Double> attribute1Values =campaign.collect("attribute-1", Double.class);

        Assert.notEmpty(attribute1Values);
        Assert.isTrue(attribute1Values.size() == 2);

        List<BigInteger> attribute2Values = campaign.collect("attribute-2", BigInteger.class);
        Assert.notEmpty(attribute2Values);
        Assert.isTrue(attribute2Values.size() == 2);

        List<Integer> attribute3Values = campaign.collect("attribute-3", Integer.class);
        Assert.notNull(attribute3Values);
        Assert.isTrue(attribute3Values.isEmpty());
    }

}
