package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.Request.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.AlertResponseDTO;
import com.stocktrack.stocktrack.Service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<AlertResponseDTO> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    public AlertResponseDTO getAlertById(@PathVariable Long id){
        return alertService.getAlertById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertResponseDTO addAlert(@Valid @RequestBody AlertRequestDTO alertRequestDTO){
        return alertService.addAlert(alertRequestDTO) ;
    }

    @PutMapping("/{id}")
    public AlertResponseDTO updateAlertById(@PathVariable Long id, @Valid @RequestBody AlertRequestDTO alertRequestDTO){
        return alertService.updateAlertById(id, alertRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlertById(@PathVariable Long id){
        alertService.deleteAlertById(id);
    }
}
