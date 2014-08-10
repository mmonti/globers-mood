package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.model.persistent.Attribute;
import com.globant.labs.mood.repository.data.AttributeRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.AttributeService;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class AttributeServiceImpl extends AbstractService implements AttributeService {

    private static final Logger logger = LoggerFactory.getLogger(AttributeServiceImpl.class);

    @Inject
    private AttributeRepository attributeRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<Attribute> attributes() {
        return Sets.newHashSet(attributeRepository.findAll());
    }

}
