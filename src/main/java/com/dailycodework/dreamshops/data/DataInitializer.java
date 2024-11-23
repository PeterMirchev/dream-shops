package com.dailycodework.dreamshops.data;

import com.dailycodework.dreamshops.model.Role;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.RoleRepository;
import com.dailycodework.dreamshops.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");

        //createDefaultUserIfNotExists();
        createDefaultRoleIfNotExists(defaultRoles);
        //createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {

        Role role = roleRepository.findByName("ROLE_USER").get();

        for (int i = 1; i < 5; i++) {
            String defaultEmail = "user" + i + "@email.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(role));
            userRepository.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExists() {

        Role role = roleRepository.findByName("ROLE_ADMIN").get();

        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(role));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {

        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(roleRepository::save);
    }
}
