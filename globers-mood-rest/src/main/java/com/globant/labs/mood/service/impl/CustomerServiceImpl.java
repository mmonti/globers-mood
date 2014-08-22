package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.repository.data.CustomerRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.CustomerService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

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
    public Page<Customer> customers(final Pageable pageable) {
        logger.info("method=customers(), args=[pageable={}]", pageable);

        return customerRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Customer store(final Customer customer) {
        Preconditions.checkNotNull(customer, "customer is null");

        logger.info("method=store(), args=[customer={}]", customer);

        final Customer storedCustomer = customerRepository.findByName(customer.getName());
        if (storedCustomer != null) {
            logger.error("method=store() - customer name=[{}] already exist", customer.getName());
            throw new BusinessException(on("Customer with name=[{}] already exist.", customer.getName()), EXPECTATION_FAILED);
        }

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer customer(final Long customerId) {
        Preconditions.checkNotNull(customerId, "customerId is null");

        logger.info("method=customer(), args=[customerId={}]", customerId);
        return customerRepository.findOne(customerId);
    }
}
