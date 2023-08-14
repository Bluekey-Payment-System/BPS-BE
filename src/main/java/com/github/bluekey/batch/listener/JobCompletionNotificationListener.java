package com.github.bluekey.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {}

    @Override
    public void afterJob(JobExecution execution) {
        if(execution.getStatus() == BatchStatus.COMPLETED) {
            log.info("[Job] {} execution has been completed from {} to {}",
                    execution.getJobInstance().getJobName(),
                    execution.getStartTime(),
                    execution.getEndTime()
            );
        }
    }
}
