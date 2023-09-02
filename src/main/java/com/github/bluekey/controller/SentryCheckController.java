package com.github.bluekey.controller;

import com.github.bluekey.util.SlackUtil;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Deprecated
@RequiredArgsConstructor
public class SentryCheckController {
    private final SlackUtil slackUtil;
    @GetMapping("/sentry-exception-test")
    public String healthCheck() throws Exception {


        try {
            int value = 0/0;
            return "health";
        }catch (Exception e) {
            slackUtil.sendExceptionMessage();
            Sentry.captureException(e);
            throw new Exception("sentry health check validation.");
        }
    }
}
