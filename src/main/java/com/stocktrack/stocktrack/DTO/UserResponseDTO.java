package com.stocktrack.stocktrack.DTO;

import com.stocktrack.stocktrack.Model.Enum.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private RoleType role;
}
