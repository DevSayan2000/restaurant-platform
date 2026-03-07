package com.example.restaurantplatform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("prod")
public class KeepAliveScheduler {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveScheduler.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.keep-alive.url:#{null}}")
    private String externalUrl;

    @Value("${server.port:8080}")
    private int serverPort;

    /**
     * Ping every 5 minutes via the EXTERNAL Render URL to prevent free-tier spin-down.
     * Render only counts external inbound HTTP requests as activity.
     * Uses /actuator/health/liveness which is lightweight and ignores mail status.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000, initialDelay = 60 * 1000)
    public void keepAlive() {
        try {
            String url;
            if (externalUrl != null && !externalUrl.isBlank()) {
                // Use liveness endpoint to avoid mail health check bringing status DOWN
                url = externalUrl.replace("/actuator/health", "/actuator/health/liveness");
            } else {
                url = "http://localhost:" + serverPort + "/api/actuator/health/liveness";
            }
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.debug("Keep-alive ping successful (status={}): {}", response.getStatusCode(), url);
        } catch (Exception e) {
            // Even if the response is non-2xx, the inbound traffic still counts for Render
            log.warn("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}

