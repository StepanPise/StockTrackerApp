package com.stocktrack.stocktrack.Repository;

import com.stocktrack.stocktrack.Model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
