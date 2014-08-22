package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.exception.TechnicalException;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.TemplateRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.TemplateService;
import com.globant.labs.mood.service.template.TemplateProcessor;
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

    @Inject
    private TemplateProcessor templateProcessor;

    @Override
    @Transactional(readOnly = true)
    public Page<Template> templates(final Pageable pageable) {
        logger.info("method=templates(), args=[pageable=[{}]]", pageable);

        return templateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Template store(final Template template) {
        Preconditions.checkNotNull(template, "template is null");

        logger.info("method=store(), args=[template=[{}]]", template);

        final Template storedTemplate = this.templateRepository.findByName(template.getName());
        if (storedTemplate != null) {
            logger.error("method=store() - template name=[{}] already exist", template.getName());
            throw new BusinessException(on("Template with name=[{}] already exist.", template.getName()), EXPECTATION_FAILED);
        }
        return templateRepository.save(template);
    }

    @Override
    @Transactional(readOnly = false)
    public Template store(final String name, final InputStream inputStream) {
        Preconditions.checkNotNull(name, "template cannot be null");
        Preconditions.checkNotNull(inputStream, "inputStream cannot be null");

        logger.info("method=store(), args=[name=[{}], inputStream=[{}]]", name, inputStream);

        Template template;
        try {
            template = new Template();
            template.setName(name);
            template.setContent(new Blob(ByteStreams.toByteArray(inputStream)));

            logger.info("method=store() - analysing template document");
            template.setMetadata(templateProcessor.extract(template));

            return store(template);

        } catch (IOException e) {
            logger.error("method=store() - exception trying to create template name=[{}]", name);
            throw new TechnicalException(on("trying to create template with name=[{}].", name), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Template template(final Long templateId) {
        Preconditions.checkNotNull(templateId, "templateId is null");

        logger.info("method=template(), args=[templateId=[{}]]", templateId);

        return templateRepository.findOne(templateId);
    }

    @Override
    @Transactional(readOnly = true)
    public Template templateByName(final String name) {
        Preconditions.checkNotNull(name, "name is null");

        logger.info("method=templateByName(), args=[name=[{}]", name);

        final Template template = this.templateRepository.findByName(name);
        if (template == null) {
            logger.error("method=templateByName() - template name=[{}] not found", name);
            throw new BusinessException(on("Template with name=[{}] not found.", name), RESOURCE_NOT_FOUND);
        }
        return template;
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(final Long templateId) {
        Preconditions.checkNotNull(templateId, "templateId is null");

        logger.info("method=remove(), args=[templateId=[{}]", templateId);

        // = TODO : Not neede - Cascade type changed.
//        final Long count = campaignRepository.countCampaignsWithTemplate(templateId);
//        if (count > 0) {
//            throw new BusinessException(on("template with id=[{}] is associated to a campaign. Remove the campaign and try again", templateId), EXPECTATION_FAILED);
//        }

        final Template template = template(templateId);
        if (template == null) {
            logger.error("method=remove() - templateId=[{}] not found", templateId);
            throw new BusinessException(on("Template with id=[{}] not found.", templateId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=remove() - removing templateId=[{}]", templateId);
        this.templateRepository.delete(template);
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateMetadata analyze(final Long templateId) {
        Preconditions.checkNotNull(templateId, "templateId is null");

        logger.info("method=analyze(), args=[templateId=[{}]", templateId);

        final Template template = template(templateId);
        if (template == null) {
            logger.error("method=analyze() - templateId=[{}] not found", templateId);
            throw new BusinessException(on("Template with id=[{}] not found.", templateId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=analyze() - extracting template metadata.");
        return templateProcessor.extract(template);
    }

    @Override
    @Transactional(readOnly = false)
    public void setMetadata(final Long templateId, final TemplateMetadata metadata) {
        Preconditions.checkNotNull(templateId, "templateId is null");
        Preconditions.checkNotNull(metadata, "metadata is null");

        logger.info("method=setMetadata(), args=[templateId={}, metadata={}]", templateId, metadata);

        final Template template = template(templateId);
        if (template == null) {
            logger.error("method=setMetadata() - templateId=[{}] not found", templateId);
            throw new BusinessException(on("Template with id=[{}] not found.", templateId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=setMetadata() - setting metadata to templateId={}", templateId);
        template.setMetadata(metadata);
        templateRepository.save(template);
    }
}
