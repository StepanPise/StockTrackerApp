package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.Request.WebhookRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.WebhookResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Entity.Webhook;
import org.springframework.stereotype.Component;

@Component
public class WebhookMapper {

    public Webhook mapToEntity(WebhookRequestDTO dto, User user) {
        Webhook webhook = new Webhook();

        webhook.setName(dto.getName());
        webhook.setType(dto.getType());
        webhook.setUrl(dto.getUrl());
        webhook.setUser(user);

        return webhook;
    }

    public WebhookResponseDTO mapToResponseDTO(Webhook webhook) {
        return new WebhookResponseDTO(

                webhook.getId(),
                webhook.getName(),
                webhook.getType(),
                webhook.getUrl()

        );
    }
}