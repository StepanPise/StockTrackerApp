package com.stocktrack.stocktrack.DTO.Response;

import com.stocktrack.stocktrack.Entity.Enum.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponseDTO {
    private Long id;
    private String name;
    private double targetPrice;
    private ConditionType conditionType;
    private LocalDateTime createdAt;

//  IDOR (Insecure Direct Object References)
//  private Long userId;
    private String userEmail;
    private String stockTicker;
}