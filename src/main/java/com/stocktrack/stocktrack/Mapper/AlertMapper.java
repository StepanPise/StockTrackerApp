package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.AlertResponseDTO;
import com.stocktrack.stocktrack.Model.Alert;
import com.stocktrack.stocktrack.Model.Stock;
import com.stocktrack.stocktrack.Model.User;

public class AlertMapper {
    public static AlertResponseDTO mapToResponseDTO(Alert alert) {
        AlertResponseDTO dto = new AlertResponseDTO();
        dto.setId(alert.getId());
        dto.setName(alert.getName());
        dto.setTargetPrice(alert.getTargetPrice());
        dto.setConditionType(alert.getConditionType());
        dto.setCreatedAt(alert.getCreatedAt());

        dto.setUserId(alert.getUser().getId());
        dto.setUserEmail(alert.getUser().getEmail());
        dto.setStockTicker(alert.getStock().getTicker());

        return dto;
    }

    public static Alert mapToEntity(AlertRequestDTO dto, User user, Stock stock) {
        Alert alert = new Alert();
        alert.setName(dto.getName());
        alert.setTargetPrice(dto.getTargetPrice());
        alert.setConditionType(dto.getConditionType());

        alert.setUser(user);
        alert.setStock(stock);

        return alert;
    }
}