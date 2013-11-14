package com.globant.labs.mood.model;

import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class DispatchResult {

    private Set<MailMessage> notifications;
    private Set<MailMessage> pendingNotifications;

    /**
     *
     * @param notifications
     */
    public DispatchResult(final Set<MailMessage> notifications) {
        this.notifications = notifications;
        this.pendingNotifications = new HashSet<MailMessage>();
    }

    public boolean hasPendingNotifications() {
        return this.getPendingNotifications().size() > 0;
    }

    public void addAsPendingNotification(final MailMessage mailMessage) {
        Preconditions.checkNotNull(mailMessage);
        this.pendingNotifications.add(mailMessage);
    }

    public Set<MailMessage> getNotifications() {
        return notifications;
    }

    public Set<MailMessage> getPendingNotifications() {
        return pendingNotifications;
    }
}
