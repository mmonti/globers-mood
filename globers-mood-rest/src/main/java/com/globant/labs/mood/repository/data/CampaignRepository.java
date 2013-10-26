package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Campaign;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface CampaignRepository extends GenericRepository<Campaign, Long> {

    @Query("select c from Campaign c order by c.feedbackNumber desc")
    List<Campaign> mostActive();
}