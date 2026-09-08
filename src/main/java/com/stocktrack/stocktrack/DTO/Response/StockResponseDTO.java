package com.stocktrack.stocktrack.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private Long id;
    private String name;
    private String ticker;
}