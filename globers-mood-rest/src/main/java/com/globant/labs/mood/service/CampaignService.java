package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Campaign;

import java.util.List;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CampaignService {

    /**
     * @return
     */
    Set<Campaign> campaigns();

    /**
     * @param campaign
     * @return
     */
    Campaign store(final Campaign campaign);

    /**
     * @param id
     * @return
     */
    Campaign campaign(final long id);

    /**
     *
     * @return
     */
    List<Campaign> mostActive();

    /**
     *
     * @param campaignId
     */
    void start(final long campaignId);

    /**
     *
     * @param campaignId
     */
    void close(final long campaignId);

    /**
     *
     * @return
     */
    void scheduledReadyToStart();

    /**
     *
     * @return
     */
    Set<Campaign> scheduledPendingToStart();

    /**
     *
     */
    void scheduledReadyToClose();

    /**
     *
     * @return
     */
    Set<Campaign> scheduledNextToExpire();

}
