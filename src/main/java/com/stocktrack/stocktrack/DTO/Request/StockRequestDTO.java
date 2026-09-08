package com.stocktrack.stocktrack.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDTO {

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Ticker is required")
    @Size(min = 1, max = 10, message = "Ticker must be between 1 and 10 characters")
    private String ticker;

}
