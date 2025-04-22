package com.ecomm_alten.back.controller;

import com.ecomm_alten.back.dto.CartItemDto;
import com.ecomm_alten.back.mapper.CartItemMapper;
import com.ecomm_alten.back.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemMapper cartItemMapper;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart(Principal principal) {
        List<CartItemDto> cartItems = cartService.getCart(principal.getName())
                .stream()
                .map(cartItemMapper::toDto)
                .toList();
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<CartItemDto> add(@PathVariable Long productId,
                                           @RequestParam(defaultValue = "1") int qty,
                                           Principal principal) {
        var item = cartService.addToCart(principal.getName(), productId, qty);
        CartItemDto cartItemDto = cartItemMapper.toDto(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long productId,
                                       Principal principal) {
        cartService.removeFromCart(principal.getName(), productId);
        return ResponseEntity.noContent().build();
    }
}
