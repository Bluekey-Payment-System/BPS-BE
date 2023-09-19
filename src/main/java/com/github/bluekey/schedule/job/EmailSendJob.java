package com.github.bluekey.schedule.job;

import com.github.bluekey.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendJob extends QuartzJobBean {
	private final EmailService emailService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("[EmailSendJob]");
		try {
			emailService.sendNotificationEmail();
		} catch (Exception e) {
			log.error("메일 전송 중 오류가 발생했습니다: {}", e.getMessage());
		}
	}
}
