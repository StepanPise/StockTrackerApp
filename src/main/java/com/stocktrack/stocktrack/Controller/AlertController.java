package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.Request.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.AlertResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    // ----------------- USER METHODS -----------------

    @GetMapping("/me")
    public List<AlertResponseDTO> getMyAlerts(@AuthenticationPrincipal User currentUser) {
        return alertService.getMyAlerts(currentUser);
    }

    @GetMapping("/{id}")
    public AlertResponseDTO getAlertById(@PathVariable Long id, @AuthenticationPrincipal User currentUser){
        return alertService.getAlertById(id, currentUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertResponseDTO addAlert(@Valid @RequestBody AlertRequestDTO alertRequestDTO, @AuthenticationPrincipal User currentUser){
        return alertService.addAlert(alertRequestDTO, currentUser);
    }

    @PutMapping("/{id}")
    public AlertResponseDTO updateAlertById(@PathVariable Long id, @Valid @RequestBody AlertRequestDTO alertRequestDTO, @AuthenticationPrincipal User currentUser){
        return alertService.updateAlertById(id, alertRequestDTO, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlertById(@PathVariable Long id, @AuthenticationPrincipal User currentUser){
        alertService.deleteAlertById(id, currentUser);
    }

    // ----------------- ADMIN METHODS -----------------

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AlertResponseDTO> getAllAlerts() {
        return alertService.getAllAlerts();
    }
}
