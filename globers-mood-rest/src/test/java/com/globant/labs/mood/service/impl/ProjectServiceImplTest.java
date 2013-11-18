package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.ProjectRepository;
import com.globant.labs.mood.service.CustomerService;
import com.globant.labs.mood.service.ProjectService;
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
import java.util.Set;


/**
* @author mauro.monti (monti.mauro@gmail.com)
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class, loader=AnnotationConfigContextLoader.class)
public class ProjectServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private ProjectService projectService;
    @Inject
    private CustomerService customerService;
    @Inject
    private UserService userService;
    @Inject
    private ProjectRepository projectRepository;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testFindByCustomer() throws Exception {
        final Customer customer1 = new Customer("Customer1");
        final Customer storedCustomer1 = customerService.store(customer1);
        final Customer customer2 = new Customer("Customer2");
        final Customer storedCustomer2 = customerService.store(customer2);

        final Project project1 = new Project("Project1", storedCustomer1);
        final Project storedProject1 = projectService.store(project1);
        final Project project2 = new Project("Project2", storedCustomer2);
        final Project storedProject2 = projectService.store(project2);

        Project project = projectRepository.projectByCustomer(storedCustomer1);
        Assert.notNull(project);
    }

    @Test
    public void testAddProject() throws Exception {
        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        final Project storedProject = projectService.store(project);

        Assert.notNull(storedProject);
        Assert.notNull(storedProject.getId());
        Assert.isTrue(storedProject.getName().equals(project.getName()));
    }

    @Test
    public void testProjects() throws Exception {
        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        final Project storedProject = projectService.store(project);

        final Page<Project> projects = projectService.projects(new PageRequest(0, 100));
        Assert.notNull(projects);
        Assert.notEmpty(projects.getContent());
    }

    @Test
    public void testProjectById() throws Exception {
        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        final Project storedProject = projectService.store(project);

        final Project result = projectService.project(storedProject.getId());
        Assert.notNull(result);
        Assert.isTrue(storedProject.getName().equals(result.getName()));
    }


    @Test
    public void testAssignUserToProject() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        final Project storedProject = projectService.store(project);

        final Boolean result = projectService.assign(storedProject.getId(), storedUser.getId());
        Assert.notNull(result);
        Assert.isTrue(result);

//        final Project resultProject = projectService.project(storedProject.getKey().getId());
//        Assert.notEmpty(resultProject.getUsers());
//        Assert.isTrue(resultProject.getUsers().contains(user));
    }

    @Test
    public void testReleaseUserFromProject() throws Exception {
        final User user = new User("Mauro Monti", "mauro.monti@globant.com");
        final User storedUser = userService.store(user);

        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Project project = new Project("Project", storedCustomer);
        final Project storedProject = projectService.store(project);

        final Boolean assignResult = projectService.assign(storedProject.getId(), storedUser.getId());
        Assert.notNull(assignResult);
        Assert.isTrue(assignResult);

//        final Project assignResultProject = projectService.project(storedProject.getKey().getId());
//        Assert.notEmpty(assignResultProject.getUsers());
//        Assert.isTrue(assignResultProject.getUsers().contains(user));

        final Boolean releaseResult = projectService.release(storedProject.getId(), storedUser.getId());
        Assert.notNull(releaseResult);
        Assert.isTrue(releaseResult);

//        final Project releaseResultProject = projectService.project(storedProject.getKey().getId());
//        Assert.isTrue(!releaseResultProject.getUsers().contains(user));
//        Assert.isTrue(releaseResultProject.getUsers().size() == 0);
    }
}
