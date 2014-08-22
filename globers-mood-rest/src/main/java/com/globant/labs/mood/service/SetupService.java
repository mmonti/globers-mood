package com.globant.labs.mood.service;

import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface SetupService {

    /**
     *
     */
    boolean wipeDataStore();

    /**
     * @return
     */
    InputStream backup();

    /**
     * @param campaignId
     * @return
     */
    InputStream backupCampaign(final Long campaignId);

}
