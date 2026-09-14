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

    public boolean sendAlertNotification(Alert alert, double currentPrice) {

        NotificationSettings settings = notificationSettingsRepository
                .findByUser(alert.getUser())
                .orElse(null);

        if (settings == null) {
            return false;
        }

        if (settings.getType() == NotificationType.EMAIL) {
            sendEmail(settings, alert, currentPrice);
            return true;

        } else if (settings.getType() == NotificationType.WEBHOOK) {
            sendWebhook(settings, alert, currentPrice);
            return true;
        }

        return false;
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
                "**%s** alert triggered!\n" +
                        "Current price: %.2f\n" +
                        "Condition: %s %.2f",
                alert.getStock().getTicker(),
                currentPrice,
                alert.getConditionType(),
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