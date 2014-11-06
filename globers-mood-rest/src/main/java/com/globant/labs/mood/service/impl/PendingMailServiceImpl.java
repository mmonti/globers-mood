package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.model.persistent.PendingMail;
import com.globant.labs.mood.repository.data.PendingMailRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.PendingMailService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class PendingMailServiceImpl extends AbstractService implements PendingMailService {

    private static final Logger logger = LoggerFactory.getLogger(PendingMailServiceImpl.class);

    @Inject
    private PendingMailRepository pendingMailRepository;

    @Transactional
    @Override
    public PendingMail store(final PendingMail pendingMail) {
        Preconditions.checkNotNull(pendingMail, "pendingMail is null");

        logger.info("method=store(), args=[pendingMail=[{}]]", pendingMail);

        return pendingMailRepository.save(pendingMail);
    }
}
