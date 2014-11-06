package com.globant.labs.mood.model.mail;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class UserDispatchResult extends CampaignDispatchResult {

    private User user;

    /**
     * @param campaign
     * @param user
     */
    public UserDispatchResult(final Campaign campaign, final User user) {
        super(campaign);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
