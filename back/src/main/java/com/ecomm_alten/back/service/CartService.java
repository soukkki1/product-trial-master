package com.ecomm_alten.back.service;

import com.ecomm_alten.back.exception.ProductNotFoundException;
import com.ecomm_alten.back.exception.UserNotFoundException;
import com.ecomm_alten.back.model.CartItem;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.repository.CartItemRepository;
import com.ecomm_alten.back.repository.ProductRepository;
import com.ecomm_alten.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class CartService {
    private final CartItemRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductRepository prodRepo;

    public List<CartItem> getCart(String userEmail) {
        log.info("Fetching cart for user with email: {}", userEmail);
        if (!userRepo.existsByEmail(userEmail)) {
            log.error("User with email {} not found", userEmail);
            throw new UserNotFoundException("User with email " + userEmail + " not found");
        }
        List<CartItem> cartItems = cartRepo.findByUserEmail(userEmail);
        log.info("Retrieved {} items for user with email: {}", cartItems.size(), userEmail);
        return cartItems;
    }

    @Transactional
    public CartItem addToCart(String userEmail, Long productId, int qty) {
        log.info("Adding product with ID {} to cart for user with email: {}, quantity: {}", productId, userEmail, qty);
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("User with email {} not found", userEmail);
                    return new UserNotFoundException("User with email " + userEmail + " not found");
                });
        Product p = prodRepo.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", productId);
                    return new ProductNotFoundException("Product with ID " + productId + " not found");
                });

        var existing = cartRepo.findByUserEmailAndProductId(userEmail, productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            log.info("Product with ID {} already in cart for user with email: {}. Increasing quantity by {}", productId, userEmail, qty);
            item.setQuantity(item.getQuantity() + qty);
            return cartRepo.save(item);
        } else {
            log.info("Product with ID {} not in cart for user with email: {}. Creating new cart item with quantity {}", productId, userEmail, qty);
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(p);
            item.setQuantity(qty);
            return cartRepo.save(item);
        }
    }

    @Transactional
    public void removeFromCart(String userEmail, Long productId) {
        log.info("Removing product with ID {} from cart for user with email: {}", productId, userEmail);
        if (!userRepo.existsByEmail(userEmail)) {
            log.error("User with email {} not found", userEmail);
            throw new UserNotFoundException("User with email " + userEmail + " not found");
        }
        CartItem item = cartRepo.findByUserEmailAndProductId(userEmail, productId)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found in the cart for user with email: {}", productId, userEmail);
                    return new ProductNotFoundException("Product with ID " + productId + " not found in the cart");
                });
        cartRepo.delete(item);
        log.info("Removed product with ID {} from cart for user with email: {}", productId, userEmail);
    }
}