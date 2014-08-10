package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.exception.TechnicalException;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.repository.data.TemplateRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.TemplateService;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.exception.BusinessException.ErrorCode.RESOURCE_NOT_FOUND;
import static com.globant.labs.mood.support.StringSupport.on;

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
    public Page<Template> templates(final Pageable pageable) {
        return templateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Template store(final Template template) {
        Preconditions.checkNotNull(template, "template cannot be null");
        final Template storedTemplate = this.templateRepository.findByName(template.getName());
        if (storedTemplate != null) {
            logger.debug("store - template with name=[{}] already existent", template.getName());
            throw new BusinessException(on("template with name=[{}] already existent.", template.getName()), EXPECTATION_FAILED);
        }
        return templateRepository.save(template);
    }

    @Override
    @Transactional(readOnly = false)
    public Template store(final String name, final InputStream inputStream) {
        Preconditions.checkNotNull(name, "template cannot be null");
        Preconditions.checkNotNull(inputStream, "inputStream cannot be null");

        Template template;
        try {
            template = new Template();
            template.setName(name);
            template.setFile(new Blob(ByteStreams.toByteArray(inputStream)));

            return store(template);

        } catch (IOException e) {
            logger.debug("There was an exception trying to create the template file name=[{}]", name);
            throw new TechnicalException(on("trying to create template file name=[{}].", name), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Template template(final long id) {
        return templateRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Template templateByName(final String name) {
        Preconditions.checkNotNull(name, "name cannot be null");
        final Template template = this.templateRepository.findByName(name);
        if (template == null) {
            throw new BusinessException(on("template with name=[{}] not found.", template.getName()), RESOURCE_NOT_FOUND);
        }
        return template;
    }

}
