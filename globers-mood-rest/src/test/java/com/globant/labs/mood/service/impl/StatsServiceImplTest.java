package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.model.statistics.WeeklyFeedback;
import com.globant.labs.mood.service.*;
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
import java.util.List;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class, loader = AnnotationConfigContextLoader.class)
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
    private StatisticsService statsService;
    @Inject
    private TokenGenerator tokenGenerator;
    @Inject
    private PreferenceService preferenceService;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();

        final Preference preference = new Preference(PreferenceKey.MAIL_SENDER, "mail@globant.com");
        preferenceService.store(preference);

        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);
        final User user1 = new User("Mauro Monti", "monti.mauro@gmail.com");
        final User storedUser1 = userService.store(user1);
        final User user2 = new User("Mauro Monti", "mauro_monti@hotmail.com");
        final User storedUser2 = userService.store(user2);

        final Template template = new Template();
        template.setName("Template");
        template.setContent(new Blob("This is an array of bytes".getBytes()));
        final Template storedTemplate = templateService.store(template);

        final Campaign campaign1 = new Campaign("Campaign1");
        campaign1.setTemplate(storedTemplate);
        campaign1.addTarget(storedUser);
        campaign1.addTarget(storedUser1);
        campaign1.addTarget(storedUser2);
        final Campaign storedCampaign1 = campaignService.store(campaign1);

        storedCampaign1.start().waitForFeedback();
        campaignService.store(storedCampaign1);

        final Campaign campaign2 = new Campaign("Campaign");
        campaign2.setTemplate(storedTemplate);
        campaign2.addTarget(storedUser);
        campaign2.addTarget(storedUser1);
        campaign2.addTarget(storedUser2);
        final Campaign storedCampaign2 = campaignService.store(campaign2);

        storedCampaign2.start().waitForFeedback();
        campaignService.store(storedCampaign2);

        final Set<Attribute> attributes = Sets.newHashSet(new Attribute("key", "value"));

        final String token = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign1, storedUser);
        final FeedbackContent feedbackContent = new FeedbackContent(storedCampaign1.getId(), storedUser.getEmail(), token, attributes);
        final Feedback storedFeedback = feedbackService.store(feedbackContent);

        final String token1 = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign1, storedUser1);
        final FeedbackContent feedbackContent1 = new FeedbackContent(storedCampaign1.getId(), storedUser1.getEmail(), token1, attributes);
        final Feedback storedFeedback1 = feedbackService.store(feedbackContent1);

        final String token2 = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign1, storedUser2);
        final FeedbackContent feedbackContent2 = new FeedbackContent(storedCampaign1.getId(), storedUser2.getEmail(), token2, attributes);
        final Feedback storedFeedback2 = feedbackService.store(feedbackContent2);

        final String token3 = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign2, storedUser);
        final FeedbackContent feedbackContent3 = new FeedbackContent(storedCampaign2.getId(), storedUser.getEmail(), token3, attributes);
        final Feedback storedFeedback3 = feedbackService.store(feedbackContent3);

        final String token4 = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign2, storedUser1);
        final FeedbackContent feedbackContent4 = new FeedbackContent(storedCampaign2.getId(), storedUser1.getEmail(), token4, attributes);
        final Feedback storedFeedback4 = feedbackService.store(feedbackContent4);

        final String token5 = UserTokenGenerator.class.cast(tokenGenerator).getToken(storedCampaign2, storedUser2);
        final FeedbackContent feedbackContent5 = new FeedbackContent(storedCampaign2.getId(), storedUser2.getEmail(), token5, attributes);
        final Feedback storedFeedback5 = feedbackService.store(feedbackContent5);
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testWeeklyFeedback() throws Exception {
        final List<WeeklyFeedback> weeklyFeedbackReport = statsService.weeklyFeedback();
        Assert.notNull(weeklyFeedbackReport);
        Assert.notEmpty(weeklyFeedbackReport);
    }

}
