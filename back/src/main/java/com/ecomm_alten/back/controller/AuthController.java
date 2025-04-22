package com.ecomm_alten.back.controller;

import com.ecomm_alten.back.dto.JwtResponse;
import com.ecomm_alten.back.dto.LoginRequest;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.repository.UserRepository;
import com.ecomm_alten.back.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    @PostMapping
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginRequest dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtTokenProvider.createToken(dto.email());
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow();
        return ResponseEntity.ok(new JwtResponse(token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail()));
    }
}
