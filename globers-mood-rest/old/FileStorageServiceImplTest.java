package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.service.FileStorageService;
import com.globant.labs.mood.support.TransactionSupport;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
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


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class, loader = AnnotationConfigContextLoader.class)
public class FileStorageServiceImplTest extends TransactionSupport {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig());

    @Inject
    private FileStorageService fileStorageService;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testStoreFile() {
        final String fileName = "myfile.gif";
        final String fullPath = fileStorageService.store(fileName, fileName.getBytes());

        Assert.notNull(fullPath);
    }

    @Test
    public void testReadFile() {
        final String fileName = "myfile.gif";
        final String filePath = fileStorageService.store(fileName, "This is the content".getBytes());

        byte[] output = fileStorageService.getFile(filePath);
        Assert.notNull(output);
    }
}
