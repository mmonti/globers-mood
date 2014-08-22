package com.globant.labs.mood.events;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;

/**
 * Created by mmonti on 8/17/14.
 */
public class DispatchUserSuccessEvent extends AbstractDispatchEvent {

    private User user;

    /**
     * @param source
     * @param campaign
     */
    public DispatchUserSuccessEvent(final Object source, final Campaign campaign, final User user) {
        super(source, campaign);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
