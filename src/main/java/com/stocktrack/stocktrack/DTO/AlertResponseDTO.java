package com.stocktrack.stocktrack.DTO;

import com.stocktrack.stocktrack.Model.Enum.ConditionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlertResponseDTO {
    private Long id;
    private String name;
    private double targetPrice;
    private ConditionType conditionType;
    private LocalDateTime createdAt;

    private Long userId;
    private String userEmail;
    private String stockTicker;
}