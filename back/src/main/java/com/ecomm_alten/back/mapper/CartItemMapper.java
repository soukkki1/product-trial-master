package com.ecomm_alten.back.mapper;

import com.ecomm_alten.back.dto.CartItemDto;
import com.ecomm_alten.back.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "product.id",   target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price",target = "productPrice")
    CartItemDto toDto(CartItem item);
}

