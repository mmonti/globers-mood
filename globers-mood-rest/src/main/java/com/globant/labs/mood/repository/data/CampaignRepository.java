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

    /**
     * @return
     */
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
     * @param campaignId
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.parentId = ?1 order by campaign.created desc")
    List<Campaign> recursiveCampaigns(final long campaignId);

    /**
     * @param fromDate
     * @return
     */
    @Query(value = "select campaign from Campaign campaign where campaign.scheduleDate is not null and campaign.scheduleDate < ?1")
    List<Campaign> scheduledReadyToStart(final Date fromDate);

    /**
     * @param scheduleDate
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.scheduleDate is not null and campaign.scheduleDate > ?1")
    List<Campaign> scheduledPendingToStart(final Date scheduleDate);

    /**
     * @param fromDate
     * @return
     */
    @Query("select campaign from Campaign campaign where campaign.expirationDate is not null and campaign.expirationDate < ?1")
    List<Campaign> scheduledReadyToClose(final Date fromDate);

    /**
     *
     */
    @Query("select campaign from Campaign campaign where campaign.expirationDate is not null and campaign.expirationDate > ?1")
    List<Campaign> scheduledNextToExpire(final Date fromDate);

    /**
     *
     */
    @Query("select count(campaign) from Campaign campaign where campaign.template.id = ?1")
    Long countCampaignsWithTemplate(final Long templateId);

}