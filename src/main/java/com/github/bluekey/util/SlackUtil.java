package com.github.bluekey.util;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import io.sentry.Sentry;
import io.sentry.SentryTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackUtil {

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private final Slack slack = Slack.getInstance();

    public boolean sendExceptionMessage(Exception exception) {
        SentryTracer information = ((SentryTracer) Sentry.getCurrentHub().getSpan());
        String eventId = information.getEventId().toString();
        String name = information.getName();
        Payload payload = Payload.builder()
                .attachments(List.of(
                        Attachment.builder()
                                .color("ff3399") // 색상
                                .fields(List.of(
                                        Field.builder()
                                                .title("API URL")
                                                .value(name)
                                                .build(),
                                        Field.builder()
                                                .title("ERROR MESSAGE")
                                                .value(exception.getMessage())
                                                .build(),
                                        Field.builder()
                                                .title("SENTRY LINK")
                                                .value("https://bluekeymusic.sentry.io/performance/bluekey-dashboard-dev-server:" + eventId)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        try {
            slack.send(webhookUrl, payload);
        } catch (IOException e) {
            log.error("slack 메시지 발송 중 문제가 발생했습니다.", e.toString());
            throw new RuntimeException(e);
        }
        return true;
    }

}
