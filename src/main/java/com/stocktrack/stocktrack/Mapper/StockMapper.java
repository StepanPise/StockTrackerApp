package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.Request.StockRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.StockResponseDTO;
import com.stocktrack.stocktrack.Entity.Stock;

public class StockMapper {
    public static StockResponseDTO mapToResponseDTO(Stock stock) {
        StockResponseDTO dto = new StockResponseDTO();
        dto.setId(stock.getId());
        dto.setName(stock.getName());
        dto.setTicker(stock.getTicker());
        return dto;
    }

    public static Stock mapToEntity(StockRequestDTO dto) {
        Stock stock = new Stock();
        stock.setName(dto.getName());
        stock.setTicker(dto.getTicker().toUpperCase());
        return stock;
    }
}