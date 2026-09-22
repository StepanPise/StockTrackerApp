package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.Request.WebhookRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.WebhookResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Service.WebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookService webhookService;

    // ----------------- USER METHODS -----------------
    @GetMapping
    public List<WebhookResponseDTO> getMyWebhooks(@AuthenticationPrincipal User currentUser) {
        return webhookService.getMyWebhooks(currentUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WebhookResponseDTO addWebhook(@Valid @RequestBody WebhookRequestDTO requestDTO, @AuthenticationPrincipal User currentUser) {
        return webhookService.addWebhook(requestDTO, currentUser);
    }

    @PutMapping("/{id}")
    public WebhookResponseDTO updateWebhook(@PathVariable Long id, @Valid @RequestBody WebhookRequestDTO requestDTO, @AuthenticationPrincipal User currentUser) {
        return webhookService.updateWebhook(id, requestDTO, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWebhook(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        webhookService.deleteWebhook(id, currentUser);
    }
}