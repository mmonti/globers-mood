package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.PendingMail;
import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface PendingMailRepository extends GenericRepository<PendingMail, Long> {

}
