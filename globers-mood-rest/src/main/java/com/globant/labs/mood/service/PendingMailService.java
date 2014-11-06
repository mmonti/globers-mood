package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.PendingMail;
import com.globant.labs.mood.model.persistent.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface PendingMailService {

    /**
     *
     * @param pendingMail
     * @return
     */
    PendingMail store(final PendingMail pendingMail);

}
