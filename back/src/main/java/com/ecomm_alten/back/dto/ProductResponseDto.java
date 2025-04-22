package com.ecomm_alten.back.dto;

import com.ecomm_alten.back.model.Product.InventoryStatus;
import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int rating,
        long createdAt,
        long updatedAt,
        InventoryStatus inventoryStatus
) {}

