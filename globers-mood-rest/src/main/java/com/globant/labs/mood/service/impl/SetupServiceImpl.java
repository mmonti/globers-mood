package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.SetupService;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class SetupServiceImpl extends AbstractService implements SetupService {

    private static final Logger logger = LoggerFactory.getLogger(SetupServiceImpl.class);

    @Override
    public boolean wipeDataStore() {
        logger.info("method=wipeDataStore()");

        final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        final Query query = new Query(Entities.KIND_METADATA_KIND);

        for (final Entity entity : datastoreService.prepare(query).asIterable()) {
            final Transaction transaction = datastoreService.beginTransaction();
            try {
                logger.info("method=wipeDataStore() - deleting entity with key=[{}]", entity.getKey());
                datastoreService.delete(entity.getKey());
                transaction.commit();

            } catch (Exception e) {
                logger.error("method=wipeDataStore() - exception trying to delete entity with key=[{}]", entity.getKey(), e);
                transaction.rollback();
                return false;
            }
        }
        return true;
    }

    @Override
    public InputStream backup() {
        logger.info("method=backup()");

        return null;
    }

    @Override
    public InputStream backupCampaign(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");
        logger.info("method=backupCampaign(), args=[campaignId=[{}]]", campaignId);

        return null;
    }
}
