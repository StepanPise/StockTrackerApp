package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.NotificationSettingsRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.NotificationSettingsResponseDTO;
import com.stocktrack.stocktrack.Entity.NotificationSettings;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.NotificationSettingsMapper;
import com.stocktrack.stocktrack.Repository.NotificationSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService {

    private final NotificationSettingsRepository notificationSettingsRepository;
    private final NotificationSettingsMapper notificationSettingsMapper;

    // ----------------- USER METHODS -----------------

    @Transactional(readOnly = true)
    public NotificationSettingsResponseDTO getSettings(User currentUser) {
        NotificationSettings settings = notificationSettingsRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Notification settings not found for user ID: " + currentUser.getId()));

        return notificationSettingsMapper.mapToResponseDTO(settings);
    }

    @Transactional
    public NotificationSettingsResponseDTO saveSettings(NotificationSettingsRequestDTO requestDTO, User currentUser) {
        NotificationSettings settings = notificationSettingsRepository.findByUser(currentUser)
                .orElseGet(() -> notificationSettingsMapper.mapToEntity(requestDTO, currentUser));

        settings.setType(requestDTO.getType());
        settings.setEmail(requestDTO.getEmail());
        settings.setWebhookUrl(requestDTO.getWebhookUrl());
        settings.setUser(currentUser);

        NotificationSettings savedSettings = notificationSettingsRepository.save(settings);
        return notificationSettingsMapper.mapToResponseDTO(savedSettings);
    }
}