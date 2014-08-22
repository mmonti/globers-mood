package com.globant.labs.mood.events;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class DispatchUserEvent extends AbstractDispatchEvent {

    private User user;

    /**
     * @param source
     * @param campaign
     */
    public DispatchUserEvent(final Object source, final Campaign campaign, final User user) {
        super(source, campaign);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
