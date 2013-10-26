package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.Preference;
import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.service.PreferenceService;
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
public class PreferenceServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private PreferenceService service;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testAddPreference() throws Exception {
        final Preference preference = new Preference("key");
        final Preference storedPreference = service.store(preference);
        Assert.notNull(storedPreference);
        Assert.isTrue(storedPreference.getPreferenceKey().equals(preference.getPreferenceKey()));
    }

    @Test
    public void testAddPreferenceEnumKey() throws Exception {
        final Preference preference = new Preference(PreferenceKey.SENDER_ALIAS, "Admin");
        final Preference storedPreference = service.store(preference);
        Assert.notNull(storedPreference);
        Assert.isTrue(storedPreference.getPreferenceKey().equals(preference.getPreferenceKey()));
    }

    @Test
    public void testPreferences() throws Exception {
        final Preference preference1 = new Preference(PreferenceKey.SENDER_ALIAS, "Admin");
        final Preference storedPreference1 = service.store(preference1);

        final Preference preference2 = new Preference("key", "1");
        final Preference storedPreference2 = service.store(preference2);

        final Set<Preference> preferences = service.preferences();

        Assert.notNull(preferences);
        Assert.notEmpty(preferences);
        Assert.isTrue(preferences.size()==2);
    }

    @Test
    public void testPreferenceById() throws Exception {
        final Preference preference = new Preference("key", "1");
        final Preference storedPreference = service.store(preference);

        final Preference loadedPreference = service.preference(storedPreference.getId());
        Assert.notNull(loadedPreference);
        Assert.isTrue(loadedPreference.getId().equals(storedPreference.getId()));
    }

    @Test
    public void testPreferenceByKey() throws Exception {
        final Preference preference1 = new Preference(PreferenceKey.SENDER_ALIAS, "Admin");
        final Preference storedPreference1 = service.store(preference1);

        final Preference preference2 = new Preference("key", "1");
        final Preference storedPreference2 = service.store(preference2);

        final String loadedPreference1 = service.preference(PreferenceKey.SENDER_ALIAS);
        final String loadedPreference2 = service.preference(storedPreference2.getPreferenceKey(), String.class);

        Assert.notNull(loadedPreference1);
        Assert.notNull(loadedPreference2);
        Assert.isTrue(loadedPreference1.equals(storedPreference1.getPreferenceValue()));
        Assert.isTrue(loadedPreference2.equals(storedPreference2.getPreferenceValue()));
    }

}
