package com.globant.labs.mood.events;

import com.globant.labs.mood.model.StatsEntry;
import org.springframework.context.ApplicationEvent;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class StatsEvent<T> extends ApplicationEvent {

    private Class<T> type;
    private StatsEntry entry;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public StatsEvent(final Object source, Class<T> type, final StatsEntry entry) {
        super(source);
        this.entry = entry;
        this.type = type;
    }

    public StatsEntry getEntry() {
        return entry;
    }

    public Class<T> getType() {
        return type;
    }

}
