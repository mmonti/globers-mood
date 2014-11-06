package com.globant.labs.mood.model.mail;

import com.globant.labs.mood.model.persistent.Campaign;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class CampaignDispatchResult extends AbstractDispatchResult {

    private Campaign campaign;

    /**
     * @param campaign
     */
    public CampaignDispatchResult(final Campaign campaign) {
        super();
        this.campaign = campaign;
    }

    public Campaign getCampaign() {
        return campaign;
    }
}
