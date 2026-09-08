package com.stocktrack.stocktrack.DTO.Response;

import com.stocktrack.stocktrack.Entity.Enum.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private RoleType role;
    private LocalDateTime createdAt;
}
