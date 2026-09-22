package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.Enum.ConditionType;
import com.stocktrack.stocktrack.Entity.Stock;
import com.stocktrack.stocktrack.Repository.AlertRepository;
import com.stocktrack.stocktrack.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertEvaluationService {

    private final AlertRepository alertRepository;
    private final MarketDataService marketDataService;
    private final NotificationService notificationService;

    // 60 000 ms (1 minute)
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void evaluateActiveAlerts() {
        List<Alert> activeAlerts = alertRepository.findByIsActiveTrue();

        if (activeAlerts.isEmpty()) return;

        log.info("Checking {} active alerts...", activeAlerts.size());

        HashMap<Stock,Double> currentPrices = new HashMap<>();

        for (Alert alert : activeAlerts) {
            currentPrices.put((alert.getStock()), 0d);
        }
        for (Map.Entry<Stock, Double> entry : currentPrices.entrySet()) {
            double price = marketDataService.getCurrentPrice(entry.getKey().getTicker());
            entry.setValue(price);
        }

        for(Alert alert : activeAlerts){
            double currentPrice = currentPrices.get(alert.getStock());

            if (isConditionMet(alert, currentPrice)) {

                boolean notificationSent =
                        notificationService.sendAlertNotification(alert, currentPrice);

                if (notificationSent) {
                    alert.setActive(false);
                }
            }
        }
    }

    private boolean isConditionMet(Alert alert, double currentPrice) {
        if (alert.getConditionType() == ConditionType.ABOVE) {
            return currentPrice >= alert.getTargetPrice();
        } else if (alert.getConditionType() == ConditionType.BELOW) {
            return currentPrice <= alert.getTargetPrice();
        }
        return false;
    }

}