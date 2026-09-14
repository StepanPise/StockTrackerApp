package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.Enum.NotificationType;
import com.stocktrack.stocktrack.Entity.NotificationSettings;
import com.stocktrack.stocktrack.Repository.NotificationSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationSettingsRepository notificationSettingsRepository;
    private final RestTemplate restTemplate;

    public void sendAlertNotification(Alert alert, double currentPrice) {

        NotificationSettings settings = notificationSettingsRepository
                .findByUser(alert.getUser())
                .orElse(null);

        if (settings == null) {
            return;
        }

        if (settings.getType() == NotificationType.EMAIL) {
            sendEmail(settings, alert, currentPrice);

        } else if (settings.getType() == NotificationType.WEBHOOK) {
            sendWebhook(settings, alert, currentPrice);
        }
    }

    private void sendEmail(
            NotificationSettings settings,
            Alert alert,
            double currentPrice) {

        // TODO
    }

    private void sendWebhook(
            NotificationSettings settings,
            Alert alert,
            double currentPrice) {

        String message = String.format(
                "🚨 Alert triggered! %s is now %.2f. Target price: %.2f",
                alert.getStock().getTicker(),
                currentPrice,
                alert.getTargetPrice()
        );

        Map<String, String> body = Map.of(
                "content", message
        );

        restTemplate.postForEntity(
                settings.getWebhookUrl(),
                body,
                Void.class
        );

        log.info(
                "Webhook notification sent for alert ID {}",
                alert.getId()
        );
    }
}