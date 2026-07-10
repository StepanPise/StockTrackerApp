package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.UserResponseDTO;
import com.stocktrack.stocktrack.Model.User;

public class UserMapper {
    public static UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
