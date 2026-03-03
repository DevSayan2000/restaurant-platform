package com.example.restaurantplatform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("prod")
public class KeepAliveScheduler {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveScheduler.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.port:8080}")
    private int serverPort;

    // Ping every 13 minutes to prevent Render free tier spin-down (15 min inactivity timeout)
    @Scheduled(fixedRate = 13 * 60 * 1000, initialDelay = 60 * 1000)
    public void keepAlive() {
        try {
            String url = "http://localhost:" + serverPort + "/api/actuator/health";
            restTemplate.getForObject(url, String.class);
            log.debug("Keep-alive ping successful");
        } catch (Exception e) {
            log.warn("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}

