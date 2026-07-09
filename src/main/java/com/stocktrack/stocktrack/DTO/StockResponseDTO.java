package com.stocktrack.stocktrack.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StockResponseDTO {
    private Long id;
    private String name;
    private String ticker;
}