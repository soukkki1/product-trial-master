package com.ecomm_alten.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record WishListItemDto(
        @NotNull
        Long productId,

        @NotBlank
        String productName,

        @Positive
        double price
) {}
