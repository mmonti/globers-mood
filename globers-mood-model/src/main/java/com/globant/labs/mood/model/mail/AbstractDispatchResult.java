package com.globant.labs.mood.model.mail;

import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public abstract class AbstractDispatchResult implements DispatchResult {

    private Set<MailMessage> dispatched;
    private Set<MailMessage> dispatchPending;

    public AbstractDispatchResult() {
        this.dispatched = new HashSet<MailMessage>();
        this.dispatchPending = new HashSet<MailMessage>();
    }

    public boolean hasPendings() {
        return this.getDispatchPending().size() > 0;
    }

    public boolean hasDispatched() {
        return this.getDispatched().size() > 0;
    }

    public void addAsDispatched(final MailMessage mailMessage) {
        Preconditions.checkNotNull(mailMessage);
        this.dispatched.add(mailMessage);
    }

    public void addAsPendingToDispatch(final MailMessage mailMessage) {
        Preconditions.checkNotNull(mailMessage);
        this.dispatchPending.add(mailMessage);
    }

    public Set<MailMessage> getDispatched() {
        return dispatched;
    }

    public Set<MailMessage> getDispatchPending() {
        return dispatchPending;
    }
}
