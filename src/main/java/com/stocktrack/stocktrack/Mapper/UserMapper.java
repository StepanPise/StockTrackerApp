package com.stocktrack.stocktrack.Mapper;

import com.stocktrack.stocktrack.DTO.Request.UserRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.UserResponseDTO;
import com.stocktrack.stocktrack.Entity.User;

public class UserMapper {
    public static UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User mapToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());//hash later
        return user;
    }
}
