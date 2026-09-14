package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.Request.NotificationSettingsRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.NotificationSettingsResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Service.NotificationSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification-settings")
public class NotificationSettingsController {

    private final NotificationSettingsService notificationSettingsService;

    @GetMapping
    public NotificationSettingsResponseDTO getSettings(@AuthenticationPrincipal User currentUser) {
        return notificationSettingsService.getSettings(currentUser);
    }

    @PutMapping
    public NotificationSettingsResponseDTO saveSettings(
            @Valid @RequestBody NotificationSettingsRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) {
        return notificationSettingsService.saveSettings(requestDTO, currentUser);
    }
}