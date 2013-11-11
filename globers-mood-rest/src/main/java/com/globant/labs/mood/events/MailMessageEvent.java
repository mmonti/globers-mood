package com.globant.labs.mood.events;

import com.globant.labs.mood.model.MailMessage;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessageEvent<T> extends ApplicationEvent {

    private Set<MailMessage> mailMessages;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public MailMessageEvent(final Object source, final Set<MailMessage> mailMessages) {
        super(source);
        this.mailMessages = mailMessages;
    }

    public Set<MailMessage> getMailMessages() {
        return mailMessages;
    }
}
