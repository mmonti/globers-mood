package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.StatsEvent;
import com.globant.labs.mood.exception.ServiceException;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.repository.data.TemplateRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.TemplateService;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class TemplateServiceImpl extends AbstractService implements TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    @Inject
    private TemplateRepository templateRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<Template> templates() {
        return new HashSet<Template>(templateRepository.findAll());
    }

    @Override
    @Transactional
    public Template store(final Template template) {
        Preconditions.checkNotNull(template, "template cannot be null");
        publishAfterCommit(new StatsEvent(this, Template.class, StatsEntry.TEMPLATE_COUNT));
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public Template store(final String name, final InputStream inputStream) {
        Preconditions.checkNotNull(name, "template cannot be null");
        Preconditions.checkNotNull(inputStream, "inputStream cannot be null");

        final Template template;
        try {
            template = new Template();
            template.setName(name);
            template.setFile(new Blob(ByteStreams.toByteArray(inputStream)));

            return store(template);

        } catch (IOException e) {
            logger.debug("There was an exception trying to create the template file name=[{}]", name);
            throw new ServiceException("There was an exception trying to create the template file name=[{}]");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Template template(final Long id) {
        return templateRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Template templateByName(final String name) {
        Preconditions.checkNotNull(name, "name cannot be null");
        final Template template = this.templateRepository.findByName(name);
        if (template == null) {
            throw new ServiceException("There was an exception trying to retrieve the template with name=[{}]");
        }
        return template;
    }

}
