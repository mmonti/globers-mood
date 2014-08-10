package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CampaignService {

    /**
     * @param pageable
     * @return
     */
    Page<Campaign> campaigns(final Pageable pageable);

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
     * @return
     */
    List<Campaign> mostActive();

    /**
     * @param campaignId
     */
    void start(final long campaignId);

    /**
     * @param campaignId
     */
    void close(final long campaignId);

    /**
     * @return
     */
    void scheduledReadyToStart();

    /**
     * @return
     */
    Set<Campaign> scheduledPendingToStart();

    /**
     *
     */
    void scheduledReadyToClose();

    /**
     * @return
     */
    Set<Campaign> scheduledNextToExpire();

}
