package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.model.Node;
import com.globant.labs.mood.model.Stats;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.repository.data.*;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.StatsService;
import com.globant.labs.mood.service.stats.StatsHandlerManager;
import com.globant.labs.mood.service.stats.impl.CountHandler;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

}
