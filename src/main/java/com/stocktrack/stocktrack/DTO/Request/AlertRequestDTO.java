package com.stocktrack.stocktrack.DTO.Request;

import com.stocktrack.stocktrack.Entity.Enum.ConditionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertRequestDTO {

    @NotBlank(message = "Alert name is required")
    private String name;

    @NotNull(message = "Target price is required")
    @Positive(message = "Target price must be positive")
    private double targetPrice;

    @NotNull(message = "Condition type is required")
    private ConditionType conditionType;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Stock ticker is required")
    private String ticker;
}
