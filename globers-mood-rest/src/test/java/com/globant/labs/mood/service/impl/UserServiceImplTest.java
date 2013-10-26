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
public class UserServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private UserService service;
//
//    @Inject
//    private UserDAO userDAO;

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
        final User storedUser = service.store(user);

        Assert.notNull(storedUser);
        Assert.notNull(storedUser.getId());
        Assert.isTrue(storedUser.getName().equals(user.getName()));
    }

    @Test
    public void testUsers() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = service.store(user);

        final Set<User> users = service.users();
        Assert.notNull(users);
        Assert.notEmpty(users);
    }

    @Test
    public void testUserById() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = service.store(user);

        final User result = service.user(storedUser.getKey().getId());
        Assert.notNull(result);
        Assert.isTrue(storedUser.getName().equals(result.getName()));
    }

    @Test
    public void testUserByEmail() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = service.store(user);

        final User result = service.userByEmail("mauro.monti@globant.com");
        Assert.notNull(result);
        Assert.isTrue(storedUser.getEmail().equals(result.getEmail()));
    }

    @Test
    public void testAssignedUsers() throws Exception {
        final User user1 = new User("Mauro Monti", "mauro.monti@globant.com");
        user1.assign();
        final User user2 = new User("Juan Sanmarco", "juan.sanmarco@globant.com");
        user2.release();

        final User storedUser1 = service.store(user1);
        final User storedUser2 = service.store(user2);

        final Set<User> result = service.assignedUsers();
        Assert.notEmpty(result);
        Assert.isTrue(result.size()==1);
    }

}
