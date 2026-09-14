package com.stocktrack.stocktrack.DTO.Request;

import com.stocktrack.stocktrack.Entity.Enum.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingsRequestDTO {

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @Size(max = 500, message = "Webhook URL cannot exceed 500 characters")
    private String webhookUrl;
}