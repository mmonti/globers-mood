package com.globant.labs.mood.service.mail;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface UserTokenGenerator {

    /**
     *
     * @param campaign
     * @param target
     * @return
     */
    String getToken(final Campaign campaign, final User target);
}
