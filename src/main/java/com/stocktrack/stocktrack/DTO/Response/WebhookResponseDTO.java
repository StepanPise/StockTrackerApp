package com.stocktrack.stocktrack.DTO.Response;

import com.stocktrack.stocktrack.Entity.Enum.WebhookType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebhookResponseDTO {

    private Long id;
    private String name;
    private WebhookType type;
    private String url;
}