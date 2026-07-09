package com.stocktrack.stocktrack.DTO;

import com.stocktrack.stocktrack.Model.Enum.ConditionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertRequestDTO {
    @NotBlank
    private String name;

    @Positive
    private double targetPrice;

    @NotNull
    private ConditionType conditionType;

    @NotNull
    private Long userId;

    @NotNull
    private Long stockId;
}
