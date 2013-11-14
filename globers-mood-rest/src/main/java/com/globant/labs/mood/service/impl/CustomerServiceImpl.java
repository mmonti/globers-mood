package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.repository.data.CustomerRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.CustomerService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class CustomerServiceImpl extends AbstractService implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Inject
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @Override
    public Set<Customer> customers() {
        return new HashSet<Customer>(customerRepository.findAll());
    }

    @Transactional
    @Override
    public Customer store(final Customer customer) {
        Preconditions.checkNotNull(customer, "customer cannot be null");
        final Customer storedCustomer = this.customerRepository.findByName(customer.getName());
        if (storedCustomer != null) {
            logger.debug("store - customer with name=[{}] already existent", customer.getName());
            throw new BusinessException(on("customer with name=[{}] already existent.", customer.getName()), EXPECTATION_FAILED);
        }
        return this.customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer customer(final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return this.customerRepository.findOne(id);
    }
}
