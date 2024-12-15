package com.authentication.JWT_AUTH.loaders;
import com.authentication.JWT_AUTH.models.Role;
import com.authentication.JWT_AUTH.models.User;
import com.authentication.JWT_AUTH.repository.RoleRepository;
import com.authentication.JWT_AUTH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);
        System.out.println("ADMIN initialized in H2 database.");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);


        // Create an admin user
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRoles(Set.of(adminRole));
        userRepository.save(adminUser);
        System.out.println("USER initialized in H2 database.");

        // Create a regular user
        User normalUser = new User();
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode("user123"));
        normalUser.setRoles(Set.of(userRole));
        userRepository.save(normalUser);

        System.out.println("Users and roles initialized in H2 database.");
    }
}

