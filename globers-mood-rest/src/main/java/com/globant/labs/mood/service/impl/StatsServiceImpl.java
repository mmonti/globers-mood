package com.globant.labs.mood.service.impl;

import ch.lambdaj.group.Group;
import com.globant.labs.mood.model.persistent.CreatedDateType;
import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.stats.WeeklyFeedback;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.StatsService;
import com.google.appengine.api.datastore.*;
import com.google.common.base.Function;
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
public class StatsServiceImpl extends AbstractService implements StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsServiceImpl.class);

    private static final String DS_STATISTICS_TOTAL = "__Stat_Total__";
    private static final String PREFIX_RESERVED_ENTITY = "_";
    private static final String COUNT = "count";

    private DatastoreService datastore;

    @Inject
    private CampaignRepository campaignRepository;
    @Inject
    private FeedbackRepository feedbackRepository;

    @PostConstruct
    public void init() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public List<Entity> getMetaData() {
        final Query entityQuery = new Query(Entities.KIND_METADATA_KIND);
        final Iterable<Entity> entities = datastore.prepare(entityQuery).asIterable();
        final Iterable<Entity> filtered = Iterables.filter(entities, new Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return !entity.getKey().getName().startsWith(PREFIX_RESERVED_ENTITY);
            }
        });
        return newArrayList(Iterables.transform(filtered, new Function<Entity, Entity>() {
            @Override
            public Entity apply(Entity entity) {
                final String kind = entity.getKey().getName();
                final int count = datastore.prepare(new Query(kind)).countEntities(FetchOptions.Builder.withDefaults());
                entity.setProperty(COUNT, count);
                return entity;
            }
        }));
    }

    @Override
    public Entity getGlobalStatistics() {
        final Entity entity = datastore.prepare(new Query(DS_STATISTICS_TOTAL)).asSingleEntity();
        return entity;
    }

    @Override
    public List<WeeklyFeedback> weeklyFeedback() {
        final Date fromDate = now().minusDays(7).withTimeAtStartOfDay().toDate();
        final List<Feedback> feedbackFromDate = feedbackRepository.feedbackFromDate(fromDate);

        // == Grouped by Campaign and Date.
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

}
