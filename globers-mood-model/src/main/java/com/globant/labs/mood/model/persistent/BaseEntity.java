package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity implements Identity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date created;

    public BaseEntity() {
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getCreated(CreatedDateType createdDateType) {
        if (CreatedDateType.DATE.equals(createdDateType)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.created);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        return created;
    }
}
