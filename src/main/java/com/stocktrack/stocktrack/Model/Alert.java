package com.stocktrack.stocktrack.Model;

import com.stocktrack.stocktrack.Model.Enum.ConditionType;
import jakarta.persistence.*;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private String name;
    private double targetPrice;
    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
