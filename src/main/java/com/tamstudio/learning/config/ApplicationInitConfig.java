package com.tamstudio.learning.config;

import com.tamstudio.learning.entity.User;
import com.tamstudio.learning.enums.Role;
import com.tamstudio.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                // Create admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                // admin.setRoles(roles);
                userRepository.save(admin);
            }
        };
    }
}
