package com.stocktrack.stocktrack.Repository;

import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Entity.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookRepository extends JpaRepository<Webhook, Long> {

    List<Webhook> findByUser(User user);
}