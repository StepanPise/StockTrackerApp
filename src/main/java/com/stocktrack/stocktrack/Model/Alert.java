package com.stocktrack.stocktrack.Model;

import com.stocktrack.stocktrack.Model.Enum.ConditionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @NotBlank
    private String name;

    @Positive
    private double targetPrice;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ConditionType conditionType;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
