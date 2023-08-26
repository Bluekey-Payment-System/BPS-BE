package com.github.bluekey.mail;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	private static final String fromName = "Bluekey music";

	@Async
	public void sendMail(String to, String title, String content)
			throws MessagingException, MailException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

		InternetAddress mailFrom = new InternetAddress(from, fromName);

		helper.setFrom(mailFrom);
		helper.setTo(to);
		helper.setSubject(title);
		helper.setText(content);

		// TODO: 메일 template 적용 필요

		try {
			mailSender.send(message);
			log.info("[{}] 정산 알림 메일 전송 완료", to);
		} catch (MailException e) {
			log.error("[{}] 메일 전송 중 오류가 발생했습니다: {}", to, e.getMessage());
		}
	}
}
