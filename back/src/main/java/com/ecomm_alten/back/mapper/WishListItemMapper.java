package com.ecomm_alten.back.mapper;

import com.ecomm_alten.back.dto.WishListItemDto;
import com.ecomm_alten.back.model.WishListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishListItemMapper {
    @Mapping(source = "product.id",   target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price",target = "price")
    WishListItemDto toDto(WishListItem item);
}
