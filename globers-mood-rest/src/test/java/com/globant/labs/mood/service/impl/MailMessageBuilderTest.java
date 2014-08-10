package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.mail.MailMessage;
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
import java.util.Date;
import java.util.Set;


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class, loader = AnnotationConfigContextLoader.class)
public class MailMessageBuilderTest extends TransactionSupport {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    public MailMessageBuilderTest() {
    }

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

    private Campaign storedCampaign1;
    private Campaign storedCampaign2;

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

        final Template template1 = new Template();
        template1.setName("template-1");
        template1.setFile(new Blob("name=[{{target.name}}]".getBytes()));

        final Template template2 = new Template();
        template2.setName("template-2");
        template2.setFile(new Blob("name=[{{target.name}}, date={{context.date}}]".getBytes()));

        final Template storedTemplate1 = templateService.store(template1);
        final Template storedTemplate2 = templateService.store(template2);

        final Campaign campaign1 = new Campaign("Campaign1");
        campaign1.addTarget(storedUser);
        campaign1.setTemplate(storedTemplate1);

        final Campaign campaign2 = new Campaign("Campaign2");
        campaign2.addTarget(storedUser);
        campaign2.setTemplate(storedTemplate2);

        final Preference senderAlias = new Preference(PreferenceKey.MAIL_SENDER_ALIAS, "alias");
        final Preference senderMail = new Preference(PreferenceKey.MAIL_SENDER, "mail");
        final Preference mailSubject = new Preference(PreferenceKey.MAIL_SUBJECT, "subject");

        preferenceService.store(senderAlias);
        preferenceService.store(senderMail);
        preferenceService.store(mailSubject);

        this.storedCampaign1 = campaignService.store(campaign1);
        this.storedCampaign2 = campaignService.store(campaign2);
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testMailMessageBuilder() throws Exception {
        final Set<MailMessage> messages = mailMessageFactory.create(storedCampaign1);
        Assert.notEmpty(messages);
    }

    @Test
    public void testMailMessageContent() throws Exception {
        final Set<MailMessage> messages = mailMessageFactory.create(storedCampaign1);
        final MailMessage mailMessage = messages.iterator().next();
        Assert.notNull(mailMessage);

        final String content = mailMessage.getContent();
        Assert.notNull(content);
        Assert.isTrue("name=[Mauro Monti]".equals(content));
    }

    @Test
    public void testMailMessageContentContextValues() throws Exception {
        final Set<MailMessage> messages = mailMessageFactory.create(storedCampaign2);
        final MailMessage mailMessage = messages.iterator().next();
        mailMessage.getContext().add("date", new Date());

        Assert.notNull(mailMessage);

        final String content = mailMessage.getContent();
        Assert.notNull(content);
    }
}
