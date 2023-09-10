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
    // 매월 1일 오전 9시에 실행
    private static final String EMAIL_SEND_CRON_CONFIG = "0 9 1 * *";
    // 매월 1일 오전 9시에 실행
    private static final String EMAIL_SEND_CRON_CONFIG_DEV_VER = "0 9 1 * *";
    // 실행 후 10분마다 실행
    private static final String EMAIL_SEND_CRON_CONFIG_LOCAL_VER = "0 */1 * * * ?";
    private static final String PRODUCTION = "prod";
    private static final String DEV = "dev";
    @Value("${spring.config.activate.on-profile}")
    private String profile;
    private final Scheduler scheduler;

    @PostConstruct
    public void run(){
        JobDetail detail = runJobDetail(EmailSendJob.class, new HashMap<>());

        try {
            if (profile.equals(PRODUCTION))
                scheduler.scheduleJob(detail, runJobTrigger(EMAIL_SEND_CRON_CONFIG));
            else if (profile.equals(DEV))
                scheduler.scheduleJob(detail, runJobTrigger(EMAIL_SEND_CRON_CONFIG_DEV_VER));
            else
                scheduler.scheduleJob(detail, runJobTrigger(EMAIL_SEND_CRON_CONFIG_LOCAL_VER));
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
