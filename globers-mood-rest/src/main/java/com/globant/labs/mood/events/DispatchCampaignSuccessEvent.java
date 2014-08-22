package com.globant.labs.mood.events;

import com.globant.labs.mood.model.persistent.Campaign;

/**
 * Created by mmonti on 8/17/14.
 */
public class DispatchCampaignSuccessEvent extends AbstractDispatchEvent {

    /**
     * @param source
     * @param campaign
     */
    public DispatchCampaignSuccessEvent(final Object source, final Campaign campaign) {
        super(source, campaign);
    }

}
