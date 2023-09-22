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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
	private static final String EMAIL_TITLE = "Bluekey music 정산 알림";
	private static final String PRODUCTION = "prod";
	private final EmailSender emailSender;
	private final MemberRepository memberRepository;
	@Value("${bluekey.profile}")
	private String profile;

	public void sendNotificationEmail() throws MessagingException, IOException {
		String year = String.valueOf(LocalDateTime.now().getYear());
		String month = String.valueOf(LocalDateTime.now().getMonthValue());

		List<Member> artists = memberRepository.findMembersByRoleAndIsRemovedFalse(MemberRole.ARTIST);
		artists.forEach(artist -> {
			try {
				if (profile.equals(PRODUCTION)) {
					emailSender.sendMail(artist.getEmail().getValue(), EMAIL_TITLE,
							artist.getName(), year, month);
				} else {
					log.info("[{}] {}에게 메일을 전송합니다.", profile, artist.getName());
				}
			} catch (MessagingException | IOException e) {
				log.error("{}에게 메일 전송 실패 : {}", artist.getName(), e.getMessage());
			}
		});
	}
}
