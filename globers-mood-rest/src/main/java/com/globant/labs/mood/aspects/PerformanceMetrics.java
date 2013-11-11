package com.globant.labs.mood.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
//@Component
//@Aspect
public class PerformanceMetrics {

    private static String entryMsgPrefix = "entering method";
    private static String exitMsgPrefix = "exiting method";

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("within(com.globant.labs.mood.service..*) && execution(public * *(..))")
    public Object log(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable  {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Logger logger = (proceedingJoinPoint.getTarget() == null) ? LoggerFactory.getLogger(PerformanceMetrics.class) : LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass());
        trace(logger, true, proceedingJoinPoint, null, 0);

        final Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        long elapsedTime = stopWatch.getTotalTimeMillis();
        if(logger.isInfoEnabled()){
            if (elapsedTime > 500) {
                logger.info("slow call - more than 500ms [{}] executionTime=[{}]", proceedingJoinPoint.toShortString(), elapsedTime);
            }
        }
        trace(logger, false, proceedingJoinPoint, result, elapsedTime);

        return result;
    }

    /**
     *
     * @param logger
     * @param entry
     * @param call
     * @param invocationResult
     * @param elapsedTime
     */
    public void trace(final Logger logger, final boolean entry, final ProceedingJoinPoint call, final Object invocationResult, final long elapsedTime) {
        if (entry) {
            logger.debug("{} [{}] with param=[{}]", entryMsgPrefix, call.toShortString(), call.getArgs());
        } else {
            logger.debug("{} [{}] with return as=[{}], execution time=[{}]", exitMsgPrefix, call.toShortString(), String.valueOf(invocationResult), elapsedTime);
        }
    }

}
