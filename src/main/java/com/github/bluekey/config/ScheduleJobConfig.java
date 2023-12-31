package com.github.bluekey.config;

import com.github.bluekey.schedule.job.EmailSendJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;


@Configuration
@RequiredArgsConstructor
public class ScheduleJobConfig {
    private static final String EXCEL_TO_DATABASE_CRON_CONFIG = "0 1 1 * *";
    private static final String EXCEL_TO_DATABASE_CRON_CONFIG_LOCAL_VER = "0 3 * * * ?";

    @Value("${bluekey.schedule.cron-config}")
    private String cronConfig;
    private final Scheduler scheduler;

    @PostConstruct
    public void run(){
        JobDetail detail = runJobDetail(EmailSendJob.class, new HashMap<>());

        try {
            scheduler.scheduleJob(detail, runJobTrigger(cronConfig));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Trigger runJobTrigger(String scheduleExp){
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail runJobDetail(Class job, Map params){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        return newJob(job).usingJobData(jobDataMap).build();
    }
}
