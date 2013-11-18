package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.service.TemplateService;
import com.google.appengine.api.datastore.Blob;
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
import java.util.Set;


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class, loader=AnnotationConfigContextLoader.class)
public class TemplateServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private TemplateService service;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testAddTemplate() throws Exception {
        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = service.store(template);
        Assert.notNull(storedTemplate);
        Assert.isTrue(storedTemplate.getName().equals(template.getName()));
    }

    @Test
    public void testTemplates() throws Exception {
        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = service.store(template);
        final Page<Template> templates = service.templates(new PageRequest(0,100));

        Assert.notNull(templates);
        Assert.notEmpty(templates.getContent());
    }

    @Test
    public void testTemplateById() throws Exception {
        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = service.store(template);
        final Template loadedTemplate = service.template(storedTemplate.getId());

        Assert.notNull(loadedTemplate);
        Assert.isTrue(loadedTemplate.getId().equals(storedTemplate.getId()));
    }

    @Test
    public void testTemplateByName() throws Exception {
        final Template template = new Template();
        template.setName("Template 1");
        template.setFile(new Blob("This is an array of bytes".getBytes()));

        Template storedTemplate = service.store(template);
        final Template loadedTemplate = service.templateByName(template.getName());

        Assert.notNull(loadedTemplate);
        Assert.isTrue(loadedTemplate.getId().equals(storedTemplate.getId()));
    }
}
