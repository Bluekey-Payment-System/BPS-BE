package com.github.bluekey.mail;

import java.io.IOException;
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
	@Value("${spring.mail.logoImg}")
	private String logoImg;
	@Value("${spring.mail.mainImg}")
	private String mainImg;
	private static final String fromName = "Bluekey music";

	@Async
	public void sendMail(String to, String title, String name, String year, String month)
			throws MessagingException, MailException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

		InternetAddress mailFrom = new InternetAddress(from, fromName);

		helper.setFrom(mailFrom);
		helper.setTo(to);
		helper.setSubject(title);

		String htmlBody = getTemplate();
		htmlBody = String.format(htmlBody, name, year, month);

		helper.setText(htmlBody, true);
		try {
			mailSender.send(message);
			log.info("[{}] 정산 알림 메일 전송 완료", to);
		} catch (MailException e) {
			log.error("[{}] 메일 전송 중 오류가 발생했습니다: {}", to, e.getMessage());
		}
	}

	private String getTemplate() {
		return "<!DOCTYPE html>\n" +
				"<html lang=\"en\">\n" +
				"  <head>\n" +
				"    <meta charset=\"UTF-8\" />\n" +
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
				+
				"  </head>\n" +
				"  <body>\n" +
				"    <div style=\"text-align: center; margin-top: 32px\">\n" +
				"      <img\n" +
				"        src=\"" + logoImg + "\"\n" +
				"      />\n" +
				"    </div>\n" +
				"    <div style=\"margin-top: 124px; text-align: center\">\n" +
				"      <img\n" +
				"        src=\"" + mainImg + "\"\n" +
				"        style=\"width: 189px\"\n" +
				"      />\n" +
				"    </div>\n" +
				"    <div\n" +
				"      style=\"\n" +
				"        margin-top: 30px;\n" +
				"        text-align: center;\n" +
				"        line-height: 24px;\n" +
				"        font-weight: 600;\n" +
				"        font-size: 20px;\n" +
				"        font-family: Pretendard;\n" +
				"        letter-spacing: 0em;\n" +
				"      \"\n" +
				"    >\n" +
				"      %S님, <br />\n" +
				"      %S년 %S월분 정산이 완료되었습니다.\n" +
				"    </div>\n" +
				"    <div\n" +
				"      style=\"\n" +
				"        margin-top: 30px;\n" +
				"        font-family: Pretendard;\n" +
				"        font-size: 14px;\n" +
				"        font-weight: 400;\n" +
				"        line-height: 20px;\n" +
				"        letter-spacing: 0em;\n" +
				"        text-align: center;\n" +
				"      \"\n" +
				"    >\n" +
				"      아래 링크로 접속하여 로그인 하면 <br />\n" +
				"      세부 내역을 확인할 수 있습니다.\n" +
				"    </div>\n" +
				"    <div\n" +
				"      style=\"\n" +
				"        margin-top: 70px;\n" +
				"        text-align: center;\n" +
				"        display: flex;\n" +
				"        justify-content: center;\n" +
				"        align-items: center;\n" +
				"      \"\n" +
				"    >\n" +
				"      <a\n" +
				"        href=\"https://bluekeyinsight.net/signin\"\n" +
				"        target=\"_blank\"\n" +
				"        style=\"\n" +
				"          display: flex;\n" +
				"          justify-content: center;\n" +
				"          align-items: center;\n" +
				"          cursor: pointer; padding: 18px 35.5px;\n" +
				"          border-radius: 6px;\n" +
				"          background: #1E1E1E;\n" +
				"        \"\n" +
				"      >\n" +
				"        <span\n" +
				"          style=\"\n" +
				"            color: white;\n" +
				"            font-family: Pretendard;\n" +
				"            font-size: 16px;\n" +
				"            font-weight: 400;\n" +
				"            line-height: 19px;\n" +
				"            letter-spacing: 0em;\n" +
				"            text-align: center;\n" +
				"          \"\n" +
				"          >정산내역 확인하러 가기</span\n" +
				"        >\n" +
				"      </a>\n" +
				"    </div>\n" +
				"  </body>\n" +
				"</html>";
	}

}
