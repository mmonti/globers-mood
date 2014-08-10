package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.service.UserService;
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


/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private UserService userService;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testAddUser() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        Assert.notNull(storedUser);
        Assert.notNull(storedUser.getId());
        Assert.isTrue(storedUser.getName().equals(user.getName()));
    }

    @Test
    public void testUsers() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Page<User> users = userService.users(new PageRequest(0, 100));
        Assert.notNull(users);
        Assert.notEmpty(users.getContent());
    }

    @Test
    public void testUserById() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final User result = userService.user(storedUser.getId());
        Assert.notNull(result);
        Assert.isTrue(storedUser.getName().equals(result.getName()));
    }

    @Test
    public void testUserByEmail() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final User result = userService.userByEmail("mauro.monti@globant.com");
        Assert.notNull(result);
        Assert.isTrue(storedUser.getEmail().equals(result.getEmail()));
    }

}
