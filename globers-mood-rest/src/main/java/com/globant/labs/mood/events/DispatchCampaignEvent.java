package com.globant.labs.mood.events;

import com.globant.labs.mood.model.persistent.Campaign;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class DispatchCampaignEvent extends AbstractDispatchEvent {

    /**
     * @param source
     * @param campaign
     */
    public DispatchCampaignEvent(final Object source, final Campaign campaign) {
        super(source, campaign);
    }

}
