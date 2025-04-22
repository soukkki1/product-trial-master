package com.ecomm_alten.back.service;

import com.ecomm_alten.back.dto.SignupRequest;
import com.ecomm_alten.back.model.Role;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.email}")
    private String adminEmail;

    @Transactional
    public void register(@NotNull SignupRequest dto) {

        List<Role> roles = new ArrayList<>();

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already in use: " + dto.email());
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setFirstname(dto.firstname());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        if (adminEmail.equalsIgnoreCase(dto.email())) {
            roles.add(Role.ADMIN);
        } else {
            roles.add(Role.USER);
        }
        user.setRoles(roles);

        userRepository.save(user);
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Convert Role to String authority
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<GrantedAuthority> auth = convertRolesToAuthorities(user.getRoles());
        if (auth == null || auth.isEmpty()) {
            auth = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                auth
        );
    }
}
