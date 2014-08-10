package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Campaign;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface CampaignRepository extends GenericRepository<Campaign, Long> {

    @Query("select campaign from Campaign campaign order by campaign.feedbackNumber desc")
    List<Campaign> mostActive();

    /**
     * @param name
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.name = ?1")
    Campaign campaignByName(final String name);

    /**
     * @param fromDate
     * @param pageable
     * @return
     */
    @Query("select campaign from Campaign campaign order by campaign.created desc")
    List<Campaign> campaignFromDate(final Date fromDate, final Pageable pageable);

    /**
     * @param fromDate
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.startDate is not null and campaign.startDate < ?1 and campaign.status in ('CREATED')")
    List<Campaign> scheduledReadyToStart(final Date fromDate);

    /**
     * @param fromDate
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.startDate is not null and campaign.startDate > :fromDate and campaign.status in ('CREATED')")
    List<Campaign> scheduledPendingToStart(final Date fromDate);

    /**
     * @param fromDate
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.endDate is not null and campaign.endDate < ?1 and campaign.status in ('CREATED', 'STARTED', 'WAITING_FOR_FEEDBACK')")
    List<Campaign> scheduledReadyToClose(final Date fromDate);

    /**
     *
     */
    @Query("select campaign from Campaign campaign where campaign.endDate is not null and campaign.endDate > ?1 and campaign.status in ('CREATED', 'STARTED', 'WAITING_FOR_FEEDBACK')")
    List<Campaign> scheduledNextToExpire(final Date fromDate);

}