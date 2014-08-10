package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.config.RootConfig;
import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.service.CustomerService;
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
public class CustomerServiceImplTest {

    private final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Inject
    private CustomerService customerService;

    @Before
    public void setUp() {
        localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        localServiceTestHelper.tearDown();
    }

    @Test
    public void testAddCustomer() throws Exception {
        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        Assert.notNull(storedCustomer);
        Assert.notNull(storedCustomer.getId());
        Assert.isTrue(storedCustomer.getName().equals(customer.getName()));
    }

    @Test
    public void testCustomers() throws Exception {
        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Page<Customer> customers = customerService.customers(new PageRequest(0, 100));
        Assert.notNull(customers);
        Assert.notEmpty(customers.getContent());
    }

    @Test
    public void testUserById() throws Exception {
        final Customer customer = new Customer("Customer");
        final Customer storedCustomer = customerService.store(customer);

        final Customer result = customerService.customer(storedCustomer.getId());
        Assert.notNull(result);
        Assert.isTrue(storedCustomer.getName().equals(result.getName()));
    }

}
