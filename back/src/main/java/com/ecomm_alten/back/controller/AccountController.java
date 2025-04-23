package com.ecomm_alten.back.controller;

import com.ecomm_alten.back.dto.SignupRequest;
import com.ecomm_alten.back.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest dto) {

        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
