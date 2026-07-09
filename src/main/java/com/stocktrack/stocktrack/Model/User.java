package com.stocktrack.stocktrack.Model;

import com.stocktrack.stocktrack.Model.Enum.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Alert> alerts;

    @Email
    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @NotBlank
    @Size(min = 6)
    private String passwordHash;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
