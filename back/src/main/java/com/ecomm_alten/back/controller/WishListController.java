package com.ecomm_alten.back.controller;

import com.ecomm_alten.back.dto.WishListItemDto;
import com.ecomm_alten.back.mapper.WishListItemMapper;
import com.ecomm_alten.back.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishService;
    private final WishListItemMapper wishListItemMapper;

    @GetMapping
    public ResponseEntity<List<WishListItemDto>> getList(Principal principal) {
        List<WishListItemDto> wishList = wishService.getWishList(principal.getName())
                .stream()
                .map(wishListItemMapper::toDto)
                .toList();
        return ResponseEntity.ok(wishList);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishListItemDto> add(@PathVariable Long productId,
                                               Principal principal) {
        var item = wishService.addToWishList(principal.getName(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListItemMapper.toDto(item));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long productId,
                                       Principal principal) {
        wishService.removeFromWishList(principal.getName(), productId);
        return ResponseEntity.noContent().build();
    }
}

