package com.globant.labs.mood.events.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.labs.mood.events.DispatchUserEvent;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.google.appengine.api.taskqueue.QueueFactory.getDefaultQueue;
import static com.google.appengine.api.taskqueue.RetryOptions.Builder.withTaskRetryLimit;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withTaskName;

/**
 * Created by mmonti on 8/13/14.
 */
public class AbstractEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEventListener.class);

    protected static final String DASH_CONSTANT = "-";
    protected static final Long CONSTANT_TASK_COUNTDOWN = 10000L;
    protected static final Long CONSTANT_TASK_MAX_BACKOFF_SECONDS = 60L;
    protected static final Integer CONSTANT_TASK_RETRY_LIMIT = 3;

    @Inject
    private ObjectMapper mapper;

    /**
     * @param object
     * @return
     */
    protected String JSONPayload(final Object object) {
        try {
            return mapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {
            logger.debug("There was an error trying to serialize the mail messages.");
        }
        return null;
    }

    /**
     * @param classType
     * @return
     */
    protected String getTaskName(final Class<?> classType) {
        final StringBuffer buffer = new StringBuffer(classType.getSimpleName());
        return buffer.append(DASH_CONSTANT).append(System.currentTimeMillis()).toString();
    }

    /**
     *
     * @param className
     * @param method
     * @param url
     */
    protected void enqueueTask(final Class<?> className, final TaskOptions.Method method, final String url) {
        final TaskOptions task = withTaskName(getTaskName(DispatchUserEvent.class));
        task.url(url);
        task.method(method);
        task.countdownMillis(CONSTANT_TASK_COUNTDOWN);
        task.retryOptions(withTaskRetryLimit(CONSTANT_TASK_RETRY_LIMIT).maxBackoffSeconds(CONSTANT_TASK_MAX_BACKOFF_SECONDS));

        getDefaultQueue().add(task);
    }
}
