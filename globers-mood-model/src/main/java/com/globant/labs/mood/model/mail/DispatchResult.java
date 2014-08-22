package com.globant.labs.mood.model.mail;

import com.globant.labs.mood.model.persistent.Campaign;

import java.util.Set;

/**
 * Created by mmonti on 8/17/14.
 */
public interface DispatchResult {

    /**
     *
     * @return
     */
    boolean hasPendings();

    /**
     *
     * @return
     */
    boolean hasDispatched();

    /**
     *
     * @param mailMessage
     */
    void addAsDispatched(final MailMessage mailMessage);

    /**
     *
     * @param mailMessage
     */
    void addAsPendingToDispatch(final MailMessage mailMessage);

    /**
     *
     * @return
     */
    Set<MailMessage> getDispatched();

    /**
     *
     * @return
     */
    Set<MailMessage> getDispatchPending();

    /**
     *
     * @return
     */
    Campaign getCampaign();
}
