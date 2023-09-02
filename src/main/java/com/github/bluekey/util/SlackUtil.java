package com.github.bluekey.util;

import com.slack.api.Slack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackUtil {

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private final Slack slack = Slack.getInstance();

    public boolean sendExceptionMessage() {
        try {
            slack.send(webhookUrl, "{\"text\":\"slack message test\"}");
        } catch (IOException e) {
            log.error("slack 메시지 발송 중 문제가 발생했습니다.", e.toString());
            throw new RuntimeException(e);
        }
        return true;
    }

}
