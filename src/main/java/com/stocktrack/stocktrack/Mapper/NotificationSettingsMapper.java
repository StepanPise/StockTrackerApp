package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.Request.NotificationSettingsRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.NotificationSettingsResponseDTO;
import com.stocktrack.stocktrack.Entity.NotificationSettings;
import com.stocktrack.stocktrack.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingsMapper {

    public NotificationSettingsResponseDTO mapToResponseDTO(NotificationSettings settings) {
        NotificationSettingsResponseDTO dto = new NotificationSettingsResponseDTO();
        dto.setId(settings.getId());
        dto.setType(settings.getType());
        dto.setEmail(settings.getEmail());
        dto.setWebhookUrl(settings.getWebhookUrl());
        return dto;
    }

    public NotificationSettings mapToEntity(NotificationSettingsRequestDTO requestDto, User user) {
        NotificationSettings settings = new NotificationSettings();
        settings.setType(requestDto.getType());
        settings.setEmail(requestDto.getEmail());
        settings.setWebhookUrl(requestDto.getWebhookUrl());
        settings.setUser(user);
        return settings;
    }
}