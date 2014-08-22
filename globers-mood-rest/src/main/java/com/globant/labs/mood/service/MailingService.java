package com.globant.labs.mood.service;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface MailingService {

    /**
     * @param campaignId
     */
    void dispatch(final Long campaignId);

    /**
     * @param campaignId
     * @param userId
     */
    void dispatch(final Long campaignId, final Long userId);
}
