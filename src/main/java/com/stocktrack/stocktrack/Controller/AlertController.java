package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.Model.Alert;
import com.stocktrack.stocktrack.Service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    public Alert getAlertById(@PathVariable Long id){
        return alertService.getAlertById(id);
    }

    @PostMapping
    public Alert addAlert(@Valid @RequestBody Alert alert){
        return alertService.addAlert(alert) ;
    }

    @PutMapping("/{id}")
    public Alert updateAlertById(@PathVariable Long id, @Valid @RequestBody Alert alert){
        return alertService.updateAlertById(id, alert);
    }

    @DeleteMapping("/{id}")
    public void deleteAlertById(@PathVariable Long id){
        alertService.deleteAlertById(id);
    }
}
