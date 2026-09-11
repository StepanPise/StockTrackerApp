package com.stocktrack.stocktrack.Config;

import com.stocktrack.stocktrack.Entity.Enum.RoleType;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSetupConfig {

    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@test.cz").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@test.cz");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(RoleType.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}