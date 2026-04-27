package com.company.attendance.config;

import com.company.attendance.entity.Role;
import com.company.attendance.entity.User;
import com.company.attendance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(User.builder()
                        .username("admin")
                        .email("admin@company.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ROLE_ADMIN)
                        .active(true)
                        .build());
            }

            if (!userRepository.existsByUsername("employee")) {
                userRepository.save(User.builder()
                        .username("employee")
                        .email("employee@company.com")
                        .password(passwordEncoder.encode("Employee@123"))
                        .role(Role.ROLE_EMPLOYEE)
                        .active(true)
                        .build());
            }
        };
    }
}
