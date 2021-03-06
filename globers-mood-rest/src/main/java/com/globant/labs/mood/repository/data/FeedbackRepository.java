package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface FeedbackRepository extends GenericRepository<Feedback, Long> {

    /**
     * @param campaign
     * @return
     */
    @Query("select feedback from Feedback feedback where feedback.campaign = ?1")
    List<Feedback> feedbackOfCampaign(final Campaign campaign);

    /**
     * @param user
     * @return
     */
    @Query("select feedback from Feedback feedback where feedback.user = ?1")
    List<Feedback> feedbackOfUser(final User user);

    /**
     * @param campaign
     * @param user
     * @return
     */
    @Query("select feedback from Feedback feedback where feedback.campaign = ?1 and feedback.user = ?2")
    Feedback feedbackOfUser(final Campaign campaign, final User user);

    /**
     * @param fromDate
     * @return
     */
    @Query("select feedback from Feedback feedback where feedback.created > ?1")
    List<Feedback> feedbackFromDate(final Date fromDate);

}