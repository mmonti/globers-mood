package com.globant.labs.mood.events;

import com.globant.labs.mood.model.mail.DispatchResult;
import org.springframework.context.ApplicationEvent;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class PendingDispatchEvent extends ApplicationEvent {

    private DispatchResult dispatchResult;

    /**
     * @param source
     * @param dispatchResult
     */
    public PendingDispatchEvent(final Object source, final DispatchResult dispatchResult) {
        super(source);
        this.dispatchResult = dispatchResult;
    }

    public DispatchResult getDispatchResult() {
        return dispatchResult;
    }
}
