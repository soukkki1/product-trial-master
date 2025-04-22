package com.ecomm_alten.back.dto;

import com.ecomm_alten.back.model.Product.InventoryStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "Name is mandatory") String name,
        @NotBlank(message = "Description is mandatory") String description,
        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
        BigDecimal price,
        @NotBlank(message = "Category cannot be blank")
        @Size(max = 100, message = "Category cannot exceed 100 characters")
        String category,
        @Min(value = 0, message = "Rating must be ≥ 0")
        @Max(value = 5, message = "Rating must be ≤ 5")
        int rating,
        @NotNull(message = "InventoryStatus is mandatory") InventoryStatus inventoryStatus
) {}
