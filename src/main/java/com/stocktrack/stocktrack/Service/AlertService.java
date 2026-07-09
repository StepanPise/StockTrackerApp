package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.Model.Alert;
import com.stocktrack.stocktrack.Repository.AlertRepository;
import com.stocktrack.stocktrack.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    //use LOMBOK insted
//    public AlertService(AlertRepository alertRepository) {
//        this.alertRepository = alertRepository;
//    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert addAlert(Alert alert) {
         return alertRepository.save(alert);
    }

    public void deleteAlertById(Long id) {
        Alert alert = getAlertById(id);
        alertRepository.delete(alert);
    }

    public Alert getAlertById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert with ID " + id + " does not exist."));
    }

    public Alert updateAlertById(Long id, Alert updatedAlert) {
        Alert existingAlert = getAlertById(id);

        existingAlert.setName(updatedAlert.getName());
        existingAlert.setTargetPrice(updatedAlert.getTargetPrice());
        existingAlert.setConditionType(updatedAlert.getConditionType());

        return alertRepository.save(existingAlert);
    }
}
