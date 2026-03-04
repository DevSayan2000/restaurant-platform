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

    @Value("${app.keep-alive.url:#{null}}")
    private String externalUrl;

    @Value("${server.port:8080}")
    private int serverPort;

    /**
     * Ping every 5 minutes via the EXTERNAL Render URL to prevent free-tier spin-down.
     * Render only counts external inbound HTTP requests as activity.
     * A localhost self-ping does NOT prevent spin-down because Render sees no external traffic.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000, initialDelay = 60 * 1000)
    public void keepAlive() {
        try {
            // Prefer external URL so Render counts it as inbound traffic
            if (externalUrl != null && !externalUrl.isBlank()) {
                restTemplate.getForObject(externalUrl, String.class);
                log.debug("Keep-alive ping to external URL successful: {}", externalUrl);
            } else {
                // Fallback to localhost (won't prevent Render spin-down but keeps app responsive)
                String url = "http://localhost:" + serverPort + "/api/actuator/health";
                restTemplate.getForObject(url, String.class);
                log.debug("Keep-alive ping to localhost successful (external URL not configured)");
            }
        } catch (Exception e) {
            log.warn("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}

