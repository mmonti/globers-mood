package com.globant.labs.mood.service.impl;

import ch.lambdaj.group.Group;
import com.globant.labs.mood.model.Node;
import com.globant.labs.mood.model.Stats;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.CreatedDateType;
import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.reports.FeedbackCountReport;
import com.globant.labs.mood.repository.data.*;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.StatsService;
import com.globant.labs.mood.service.stats.StatsHandlerManager;
import com.globant.labs.mood.service.stats.impl.CountHandler;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.group.Groups.by;
import static ch.lambdaj.group.Groups.group;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class StatsServiceImpl extends AbstractService implements StatsService {

    @Inject
    private CampaignRepository campaignRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private ProjectRepository projectRepository;
    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private TemplateRepository templateRepository;
    @Inject
    private FeedbackRepository feedbackRepository;
    @Inject
    private StatsHandlerManager statsManager;

    @PostConstruct
    public void init() {
        statsManager.register(StatsEntry.COUNT, new CountHandler(campaignRepository));
        statsManager.register(StatsEntry.CAMPAIGN_COUNT, new CountHandler(campaignRepository));
        statsManager.register(StatsEntry.USER_COUNT, new CountHandler(userRepository));
        statsManager.register(StatsEntry.PROJECT_COUNT, new CountHandler(projectRepository));
        statsManager.register(StatsEntry.CUSTOMER_COUNT, new CountHandler(customerRepository));
        statsManager.register(StatsEntry.TEMPLATE_COUNT, new CountHandler(templateRepository));
        statsManager.register(StatsEntry.FEEDBACK_COUNT, new CountHandler(feedbackRepository));
    }

    @Override
    public void executeStatEntryHandler(final StatsEntry statsEntry) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        executeStatEntryHandler(statsEntry, null);
    }

    @Override
    public <T> void executeStatEntryHandler(final StatsEntry statsEntry, final Class<T> entity) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        Preconditions.checkNotNull(entity, "entity cannot be null");

        statsManager.execute(statsEntry, entity);
    }

    @Override
    public Stats getStats() {
        return statsManager.getStats();
    }

    @Override
    public Node getStatEntry(final StatsEntry statsEntry) {
        final Stats stats = getStats();
        if (stats != null) {
            return stats.getEntry(statsEntry);
        }
        return null;
    }

    @Override
    public List<FeedbackCountReport> feedbackCountReport() {
        final Date fromDate = getDateFromDays(7, true);
        final List<FeedbackCountReport> weeklyFeedbackReports = new ArrayList<FeedbackCountReport>();
        final List<Feedback> feedbackFromDate = feedbackRepository.feedbackFromDate(fromDate);
        if (feedbackFromDate.isEmpty()) {
            final Pageable pageable = new PageRequest(0, 5);
            final List<Campaign> campaignsFromDate = campaignRepository.campaignFromDate(fromDate, pageable);
            for (final Campaign currentCampaign : campaignsFromDate) {
                weeklyFeedbackReports.add(new FeedbackCountReport(currentCampaign.getId(), fromDate, new Date()));
            }
            return weeklyFeedbackReports;
        }

        // == Grouped by Campaign and Date.
        final Group<Feedback> feedbackGroupedByDate = group(feedbackFromDate,
                by(on(Feedback.class).getCampaign().getId()),
                by(on(Feedback.class).getCreated(CreatedDateType.DATE)
        ));

        for (final Group<Feedback> feedbackByCampaign : feedbackGroupedByDate.subgroups()) {
            FeedbackCountReport weeklyFeedbackReport = null;

            for (final Feedback feedback : feedbackByCampaign.findAll()) {
                weeklyFeedbackReport = new FeedbackCountReport(feedback.getCampaign().getId(), fromDate, new Date());

                final Calendar calendar = Calendar.getInstance();
                for (int day = 0; day < 7; day++) {
                    final List<Feedback> feedbackByDate = feedbackByCampaign.find(getDateFromDays(day, true));
                    weeklyFeedbackReport.addEntry(calendar.getTime(), feedbackByDate.size());
                }
                weeklyFeedbackReports.add(weeklyFeedbackReport);
            }
        }
        return weeklyFeedbackReports;
    }

    private Date getDateFromDays(int days, boolean resetTime) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        if (resetTime) {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTime();
    }

}
