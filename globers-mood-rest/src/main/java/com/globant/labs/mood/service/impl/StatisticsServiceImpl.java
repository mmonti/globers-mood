package com.globant.labs.mood.service.impl;

import ch.lambdaj.group.Group;
import com.globant.labs.mood.model.persistent.CreatedDateType;
import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.statistics.CampaignStatistics;
import com.globant.labs.mood.model.statistics.WeeklyFeedback;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.StatisticsService;
import com.google.appengine.api.datastore.*;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.group.Groups.by;
import static ch.lambdaj.group.Groups.group;
import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.joda.time.DateTime.now;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class StatisticsServiceImpl extends AbstractService implements StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private static final String PREFIX_RESERVED_ENTITY = "_";
    private static final String COUNT = "count";

    @Inject
    private FeedbackRepository feedbackRepository;

    private DatastoreService datastoreService;

    @PostConstruct
    public void init() {
        datastoreService = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public List<Entity> datastoreEntities() {
        logger.info("method=datastoreEntities()");

        logger.info("method=datastoreEntities() - querying by [{}]", Entities.KIND_METADATA_KIND);
        final Query entityQuery = new Query(Entities.KIND_METADATA_KIND);
        final Iterable<Entity> entities = datastoreService.prepare(entityQuery).asIterable();

        logger.info("method=datastoreEntities() - filtering entities prefixed with=[{}]", PREFIX_RESERVED_ENTITY);
        final Iterable<Entity> filtered = Iterables.filter(entities, new Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return !entity.getKey().getName().startsWith(PREFIX_RESERVED_ENTITY);
            }
        });

        logger.info("method=datastoreEntities() - fetching entities count");
        return newArrayList(Iterables.transform(filtered, new Function<Entity, Entity>() {
            @Override
            public Entity apply(Entity entity) {
                final String kind = entity.getKey().getName();
                final int count = datastoreService.prepare(new Query(kind)).countEntities(FetchOptions.Builder.withDefaults());
                entity.setProperty(COUNT, count);
                return entity;
            }
        }));
    }

    @Override
    public List<WeeklyFeedback> weeklyFeedback() {
        logger.info("method=weeklyFeedback()");

        final Date fromDate = now().minusDays(7).withTimeAtStartOfDay().toDate();
        final List<Feedback> feedbackFromDate = feedbackRepository.feedbackFromDate(fromDate);

        // == Grouped by Campaign and Date.
        logger.info("method=weeklyFeedback() - grouping by date=[{}]", fromDate);
        final Group<Feedback> feedbackGroupedByDate = group(feedbackFromDate,
                by(on(Feedback.class).getCampaign().getId()),
                by(on(Feedback.class).getCreated(CreatedDateType.DATE, WeeklyFeedback.DAY_PATTERN)
                ));

        final List<WeeklyFeedback> weeklyStats = new ArrayList<WeeklyFeedback>();
        for (final Group<Feedback> campaignFeedback : feedbackGroupedByDate.subgroups()) {
            final long campaignId = Long.class.cast(campaignFeedback.key());
            final WeeklyFeedback weeklyFeedback = new WeeklyFeedback(campaignId, fromDate, now().toDate());

            for (final String key : campaignFeedback.keySet()) {
                final int size = campaignFeedback.find(key).size();
                weeklyFeedback.addEntry(key, size);
            }
            weeklyStats.add(weeklyFeedback);
        }
        return weeklyStats;
    }

    @Override
    public CampaignStatistics campaignStatistics(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=campaignStatistics(), args=[campaignId={}]", campaignId);

        return new CampaignStatistics(campaignId);
    }
}
