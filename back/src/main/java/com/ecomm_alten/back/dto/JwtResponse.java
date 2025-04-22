package com.ecomm_alten.back.dto;

public record JwtResponse(
        String token,
        String type,
        Long   id,
        String username,
        String email
) {}

