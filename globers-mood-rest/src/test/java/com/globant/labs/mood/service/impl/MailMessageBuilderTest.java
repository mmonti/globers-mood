package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.MailMessage;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.service.*;
import com.globant.labs.mood.service.mail.MailMessageFactory;
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
import java.util.Set;


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class, loader=AnnotationConfigContextLoader.class)
public class MailMessageBuilderTest extends TransactionSupport {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    public MailMessageBuilderTest() {    }

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
    private MailMessageFactory mailMessageFactory;
    @Inject
    private PreferenceService preferenceService;

    private Campaign storedCampaign;

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
        template.setName("template-1");
        template.setFile(new Blob("Name=[{{user.name}}]".getBytes()));

        final Template storedTemplate = templateService.store(template);

        final Campaign campaign = new Campaign("Campaign");
//        campaign.addProject(storedProject);
        campaign.addTarget(storedUser);
        campaign.setTemplate(storedTemplate);

        final Preference senderAlias = new Preference(PreferenceKey.SENDER_ALIAS);
        final Preference senderMail = new Preference(PreferenceKey.SENDER_MAIL);
        preferenceService.store(senderAlias);
        preferenceService.store(senderMail);

        this.storedCampaign = campaignService.store(campaign);
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testMailMessageBuilder() throws Exception {
        final Set<MailMessage> messages = mailMessageFactory.create(storedCampaign);
        Assert.notEmpty(messages);
    }

    @Test
    public void testMailMessageContent() throws Exception {
        final Set<MailMessage> messages = mailMessageFactory.create(storedCampaign);
        final MailMessage mailMessage = messages.iterator().next();
        Assert.notNull(mailMessage);

        final String content = mailMessage.getContent();
        Assert.notNull(content);
        Assert.isTrue("Name=[Mauro Monti]".equals(content));
    }
}
