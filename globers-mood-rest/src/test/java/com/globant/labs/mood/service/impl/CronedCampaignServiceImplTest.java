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
import java.util.Calendar;
import java.util.List;
import java.util.Set;


/**
* @author mauro.monti (monti.mauro@gmail.com)
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class, loader=AnnotationConfigContextLoader.class)
public class CronedCampaignServiceImplTest extends TransactionSupport {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private CampaignService campaignService;
    @Inject
    private UserService userService;
    @Inject
    private TemplateService templateService;
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
    public void testStartScheduledCampaigns() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        // = Sets the startDate;
        campaign.setStartDate(calendar.getTime());

        Campaign storedCampaign = campaignService.store(campaign);
        campaignService.startScheduledCampaigns();

        storedCampaign = campaignService.campaign(campaign.getId());
        Assert.isTrue(CampaignStatus.WAITING_FOR_FEEDBACK.equals(storedCampaign.getStatus()));
    }

    @Test
    public void testStartScheduledCampaignsWithFutureStartDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        // = Sets the startDate;
        campaign.setStartDate(calendar.getTime());

        Campaign storedCampaign = campaignService.store(campaign);
        campaignService.startScheduledCampaigns();

        storedCampaign = campaignService.campaign(campaign.getId());
        Assert.isTrue(CampaignStatus.CREATED.equals(storedCampaign.getStatus()));
    }

    @Test
    public void testStartScheduledCampaignsWithNullStartDate() throws Exception {
        Campaign storedCampaign = campaignService.store(campaign);
        campaignService.startScheduledCampaigns();

        storedCampaign = campaignService.campaign(campaign.getId());
        Assert.isTrue(CampaignStatus.CREATED.equals(storedCampaign.getStatus()));
    }

    @Test
     public void testCloseExpiredCampaigns() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        // = Sets the endDate;
        campaign.setEndDate(calendar.getTime());
        campaign.start();
        campaign.waitForFeedback();

        Campaign storedCampaign = campaignService.store(campaign);
        campaignService.closeExpiredCampaigns();

        storedCampaign = campaignService.campaign(campaign.getId());
        Assert.isTrue(CampaignStatus.CLOSED.equals(storedCampaign.getStatus()));
    }

    @Test
    public void testCloseExpiredCampaignsWithFutureExpirationDates() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        // = Sets the endDate;
        campaign.setEndDate(calendar.getTime());
        campaign.start();
        campaign.waitForFeedback();

        Campaign storedCampaign = campaignService.store(campaign);
        campaignService.closeExpiredCampaigns();

        storedCampaign = campaignService.campaign(campaign.getId());
        Assert.isTrue(CampaignStatus.WAITING_FOR_FEEDBACK.equals(storedCampaign.getStatus()));
    }

    @Test
    public void testCloseExpiredCampaignsWithNullExpirationDates() throws Exception {
        campaign.start();
        campaign.waitForFeedback();

        Campaign storedCampaign = campaignService.store(campaign);
        campaignService.closeExpiredCampaigns();

        storedCampaign = campaignService.campaign(campaign.getId());
        Assert.isTrue(CampaignStatus.WAITING_FOR_FEEDBACK.equals(storedCampaign.getStatus()));
    }
}