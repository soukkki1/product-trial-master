package com.ecomm_alten.back.mapper;

import com.ecomm_alten.back.dto.*;
import com.ecomm_alten.back.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDto dto);
    ProductResponseDto toDto(Product entity);
}