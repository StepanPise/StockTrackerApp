package com.stocktrack.stocktrack.Repository;

import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByUser(User currentUser);
}
