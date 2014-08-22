package com.globant.labs.mood.events;

import com.globant.labs.mood.model.persistent.Campaign;
import org.springframework.context.ApplicationEvent;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public abstract class AbstractDispatchEvent extends ApplicationEvent {

    private Campaign campaign;

    /**
     * @param source
     * @param campaign
     */
    public AbstractDispatchEvent(final Object source, final Campaign campaign) {
        super(source);
        this.campaign = campaign;
    }

    /**
     * @return
     */
    public Campaign getCampaign() {
        return campaign;
    }
}
