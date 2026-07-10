package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.StockResponseDTO;
import com.stocktrack.stocktrack.Model.Stock;

public class StockMapper {
    public static StockResponseDTO mapToResponseDTO(Stock stock) {
        StockResponseDTO dto = new StockResponseDTO();
        dto.setId(stock.getId());
        dto.setName(stock.getName());
        dto.setTicker(stock.getTicker());
        return dto;
    }
}