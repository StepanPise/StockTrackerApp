package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.Request.UserRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.UserResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
