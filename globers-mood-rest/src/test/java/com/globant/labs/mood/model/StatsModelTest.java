package com.globant.labs.mood.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.service.impl.ImporterServiceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class StatsModelTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void testCreateStatEntry() throws Exception {
        final Node importService = NodeBuilder.create(ImporterServiceImpl.class.getName());
        final Node customer = importService.path(Customer.class.getName());
        customer.path(Measure.ELAPSED_TIME).value(10);
        customer.path(Measure.COUNT).value(1214);

        final Node user = importService.path(User.class.getName());
        Node user1 = user.path(Measure.ELAPSED_TIME).value(232);
        user1.path(Measure.COUNT).value(211);

        final Stats stats = new Stats();
        stats.addStat(StatsEntry.MOST_ACTIVE_CAMPAIGNS, importService);
    }

}
