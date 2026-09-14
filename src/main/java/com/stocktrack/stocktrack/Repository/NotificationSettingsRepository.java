package com.stocktrack.stocktrack.Repository;

import com.stocktrack.stocktrack.Entity.NotificationSettings;
import com.stocktrack.stocktrack.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

    Optional<NotificationSettings> findByUser(User user);

    Optional<NotificationSettings> findByUserId(Long userId);
}