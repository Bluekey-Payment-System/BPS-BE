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
    private final static String SENTRY_PERFORMANCE_URL_PREFIX = "https://bluekeymusic.sentry.io/performance/bluekey-dashboard-dev-server:";
    private final static String SLACK_MESSAGE_TEMPLATE_COLOR_CODE = "ff3399";
    private final static String SLACK_ERROR_FIELD_TITLE_API_URL = "API URL";
    private final static String SLACK_ERROR_FIELD_TITLE_ERROR_MESSAGE = "ERROR MESSAGE";
    private final static String SLACK_ERROR_FIELD_TITLE_SENTRY_LINK = "SENTRY LINK";

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private final Slack slack = Slack.getInstance();

    public void sendExceptionMessage(Exception exception) {
        SentryTracer information = ((SentryTracer) Sentry.getCurrentHub().getSpan());
        String eventId = information.getEventId().toString();
        String name = information.getName();
        Payload payload = Payload.builder()
                .attachments(List.of(
                        Attachment.builder()
                                .color(SLACK_MESSAGE_TEMPLATE_COLOR_CODE) // color code
                                .fields(List.of(
                                        Field.builder()
                                                .title(SLACK_ERROR_FIELD_TITLE_API_URL)
                                                .value(name)
                                                .build(),
                                        Field.builder()
                                                .title(SLACK_ERROR_FIELD_TITLE_ERROR_MESSAGE)
                                                .value(exception.getMessage())
                                                .build(),
                                        Field.builder()
                                                .title(SLACK_ERROR_FIELD_TITLE_SENTRY_LINK)
                                                .value(SENTRY_PERFORMANCE_URL_PREFIX + eventId)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        try {
            slack.send(webhookUrl, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
