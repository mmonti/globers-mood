package com.globant.labs.mood.exception;

import com.globant.labs.mood.model.persistent.Campaign;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException(final Campaign campaign) {
        super("Campaign=["+campaign.getName()+"] - with id=["+campaign.getId()+"] is in an incorrect status=["+campaign.getStatus()+"]");
    }

}
