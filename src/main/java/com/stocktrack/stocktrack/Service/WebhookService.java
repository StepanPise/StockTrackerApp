package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.WebhookRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.WebhookResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Entity.Webhook;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.WebhookMapper;
import com.stocktrack.stocktrack.Repository.WebhookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final WebhookMapper webhookMapper;

    // ----------------- PRIVATE HELPER METHODS -----------------

    private Webhook getWebhookEntityByIdAndValidateOwnership(Long id, User currentUser) {
        Webhook webhook = webhookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webhook with ID " + id + " does not exist."));

        if (!webhook.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No authorization");
        }

        return webhook;
    }

    // ----------------- USER METHODS -----------------

    @Transactional(readOnly = true)
    public List<WebhookResponseDTO> getMyWebhooks(User currentUser) {
        return webhookRepository.findByUser(currentUser).stream()
                .map(webhookMapper::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public WebhookResponseDTO addWebhook(WebhookRequestDTO requestDTO, User currentUser) {
        Webhook webhook = webhookMapper.mapToEntity(requestDTO, currentUser);

        return webhookMapper.mapToResponseDTO(webhookRepository.save(webhook));
    }

    @Transactional
    public WebhookResponseDTO updateWebhook(Long id, WebhookRequestDTO requestDTO, User currentUser) {
        Webhook webhook = getWebhookEntityByIdAndValidateOwnership(id, currentUser);

        webhook.setName(requestDTO.getName());
        webhook.setType(requestDTO.getType());
        webhook.setUrl(requestDTO.getUrl());

        return webhookMapper.mapToResponseDTO(webhook);
    }

    @Transactional
    public void deleteWebhook(Long id, User currentUser) {
        Webhook webhook = getWebhookEntityByIdAndValidateOwnership(id, currentUser);
        webhookRepository.delete(webhook);
    }
}