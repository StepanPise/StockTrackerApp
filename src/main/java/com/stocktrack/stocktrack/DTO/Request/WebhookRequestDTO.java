package com.stocktrack.stocktrack.DTO.Request;

import com.stocktrack.stocktrack.Entity.Enum.WebhookType;
import jakarta.validation.constraints.NotBlank;
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
public class WebhookRequestDTO {

    @NotBlank(message = "Webhook name is required")
    @Size(max = 100, message = "Webhook name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Webhook type is required")
    private WebhookType type;

    @NotBlank(message = "Webhook URL is required")
    @Size(max = 500, message = "Webhook URL cannot exceed 500 characters")
    private String url;
}