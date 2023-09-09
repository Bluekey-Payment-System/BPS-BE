package com.github.bluekey.mail;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.repository.member.MemberRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
	private final EmailSender emailSender;
	private final MemberRepository memberRepository;
	@Value("${spring.config.activate.on-profile}")
	private String profile;
	private static final String DEV_TARGET_EMAIL = "wnl383@naver.com";
	private static final String DEV_TARGET_EMAIL_NAME = "조세영";
	private static final String EMAIL_TITLE = "Bluekey music 정산 알림";
	private static final String PRODUCTION = "prod";
	private static final String DEV = "dev";

	public void sendNotificationEmail() throws MessagingException, IOException {
		String year = String.valueOf(LocalDateTime.now().getYear());
		String month = String.valueOf(LocalDateTime.now().getMonthValue());

		List<Member> artists = memberRepository.findMemberByRoleAndIsRemovedFalse(MemberRole.ARTIST);
		artists.forEach(artist -> {
			try {
				if (profile.equals(PRODUCTION)) {
					emailSender.sendMail(artist.getEmail().getValue(), EMAIL_TITLE,
							artist.getName(), year, month);
				} else if (profile.equals(DEV)) {
					emailSender.sendMail(DEV_TARGET_EMAIL, EMAIL_TITLE,
							DEV_TARGET_EMAIL_NAME, year, month);
				} else {
					log.info("[local] {}에게 메일을 전송합니다.", artist.getName());
				}
			} catch (MessagingException | IOException e) {
				log.error("{}에게 메일 전송 실패 : {}", artist.getName(), e.getMessage());
			}
		});
	}
}
