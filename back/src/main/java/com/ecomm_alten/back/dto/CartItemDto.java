package com.ecomm_alten.back.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CartItemDto(

        @NotNull(message = "Product ID cannot be null")
        @Min(value = 1, message = "Product ID must be greater than or equal to 1")
        Long productId,

        @NotBlank(message = "Product name cannot be blank")
        String productName,

        @Min(value = 0, message = "Product price must be greater than or equal to 0")
        double productPrice,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {
}
