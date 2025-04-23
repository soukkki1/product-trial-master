package com.ecomm_alten.back.service;

import com.ecomm_alten.back.dto.SignupRequest;
import com.ecomm_alten.back.exception.UserAlreadyExistsException;
import com.ecomm_alten.back.exception.UserNotFoundException;
import com.ecomm_alten.back.model.Role;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.email}")
    private String adminEmail;

    @Transactional
    public void register(@NotNull SignupRequest dto) {
        log.info("Registering user with email: {}", dto.email());

        List<Role> roles = new ArrayList<>();

        if (userRepository.existsByEmail(dto.email())) {
            log.warn("Attempt to register with already used email: {}", dto.email());
            throw new UserAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setFirstname(dto.firstname());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        log.debug("User details set for email: {}", dto.email());

        if (adminEmail.equalsIgnoreCase(dto.email())) {
            roles.add(Role.ADMIN);
            log.info("Admin role assigned to email: {}", dto.email());
        } else {
            roles.add(Role.USER);
            log.info("User role assigned to email: {}", dto.email());
        }
        user.setRoles(roles);

        userRepository.save(user);
        log.info("User registered successfully with email: {}", dto.email());
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<Role> roles) {
        log.debug("Converting roles to authorities");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Convert Role to String authority
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new UserNotFoundException("User not found: " + email);
                });

        List<GrantedAuthority> auth = convertRolesToAuthorities(user.getRoles());
        if (auth == null || auth.isEmpty()) {
            log.warn("No roles found for email: {}, assigning default ROLE_USER", email);
            auth = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        log.info("User loaded successfully for email: {}", email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                auth
        );
    }
}
