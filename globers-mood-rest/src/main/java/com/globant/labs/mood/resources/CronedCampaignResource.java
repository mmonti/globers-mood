package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Campaign;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CronedCampaignResource {

    /**
     *
     * @return
     */
    Response scheduledReadyToStart();

    /**
     *
     * @return
     */
    Response scheduledPendingToStart();

    /**
     *
     * @return
     */
    Response scheduledReadyToClose();

    /**
     *
     * @return
     */
    Response scheduledNextToExpire();
}
