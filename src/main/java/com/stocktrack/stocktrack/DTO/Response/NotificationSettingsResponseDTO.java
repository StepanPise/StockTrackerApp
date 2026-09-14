package com.stocktrack.stocktrack.DTO.Response;

import com.stocktrack.stocktrack.Entity.Enum.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingsResponseDTO {

    private Long id;
    private NotificationType type;
    private String email;
    private String webhookUrl;
}