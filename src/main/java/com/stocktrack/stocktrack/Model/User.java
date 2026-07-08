package com.stocktrack.stocktrack.Model;

import com.stocktrack.stocktrack.Model.Enum.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Alert> alerts;

    private String email;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    private String passwordHash;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
