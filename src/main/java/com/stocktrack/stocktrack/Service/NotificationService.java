package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.Enum.WebhookType;
import com.stocktrack.stocktrack.Entity.Webhook;
import com.stocktrack.stocktrack.Repository.WebhookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final WebhookRepository webhookRepository;
    private final RestTemplate restTemplate;

    private void sendWebhook(Webhook webhook, Alert alert, double currentPrice) {
        String message = String.format(
                "%s alert triggered!\nCurrent price: %.2f\nCondition: %s %.2f",
                alert.getStock().getTicker(),
                currentPrice,
                alert.getConditionType(),
                alert.getTargetPrice()
        );

        switch (webhook.getType()) {
            case DISCORD -> sendDiscordWebhook(webhook, message);
            case SLACK -> sendSlackWebhook(webhook, message);
        }

        log.info("Webhook notification sent for webhook ID {} and alert ID {}", webhook.getId(), alert.getId());
    }

    private void sendDiscordWebhook(Webhook webhook, String message) {
        Map<String, String> body = Map.of("content", message);
        restTemplate.postForEntity(webhook.getUrl(), body, Void.class);
    }

    private void sendSlackWebhook(Webhook webhook, String message) {
        Map<String, String> body = Map.of("text", message);
        restTemplate.postForEntity(webhook.getUrl(), body, Void.class);
    }

    public boolean sendAlertNotification(Alert alert, double currentPrice) {
        List<Webhook> webhooks = webhookRepository.findByUser(alert.getUser());

        if (webhooks.isEmpty()) {
            log.warn("No webhooks configured for user ID {}", alert.getUser().getId());
            return false;
        }

        boolean notificationSent = false;

        for (Webhook webhook : webhooks) {
            try {
                sendWebhook(webhook, alert, currentPrice);
                notificationSent = true;
            } catch (Exception e) {
                log.error("Failed to send webhook ID {} for alert ID {}", webhook.getId(), alert.getId(), e);
            }
        }

        return notificationSent;
    }
}