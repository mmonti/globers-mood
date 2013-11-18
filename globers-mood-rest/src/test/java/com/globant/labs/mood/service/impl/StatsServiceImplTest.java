package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.model.reports.FeedbackCountReport;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.service.*;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
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
public class StatsServiceImplTest {

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
    private StatsService statsService;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private TokenGenerator tokenGenerator;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();

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
        final Feedback storedFeedback = feedbackService.store(storedCampaign.getId(), storedUser.getEmail(), token, Mood.NEUTRAL, Mood.NEUTRAL, "This is my Comment");

        final Set<Feedback> storedFeedbackOfCampaign = feedbackService.feedbackOfCampaign(storedFeedback.getCampaign().getId());
        Assert.notNull(storedFeedbackOfCampaign);
        Assert.notEmpty(storedFeedbackOfCampaign);
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testFeedbackCountReport() throws Exception {
        final List<FeedbackCountReport> weeklyFeedbackReport = statsService.feedbackCountReport();
        Assert.notNull(weeklyFeedbackReport);
        Assert.notEmpty(weeklyFeedbackReport);
    }

}
