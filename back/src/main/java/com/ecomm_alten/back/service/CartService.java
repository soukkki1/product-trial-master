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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductRepository prodRepo;

    // Updated methods with exceptions
    public List<CartItem> getCart(String userEmail) {
        if (!userRepo.existsByEmail(userEmail)) {
            throw new UserNotFoundException("User with email " + userEmail + " not found");
        }
        return cartRepo.findByUserEmail(userEmail);
    }

    @Transactional
    public CartItem addToCart(String userEmail, Long productId, int qty) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));
        Product p = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        var existing = cartRepo.findByUserEmailAndProductId(userEmail, productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + qty);
            return cartRepo.save(item);
        } else {
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(p);
            item.setQuantity(qty);
            return cartRepo.save(item);
        }
    }

    @Transactional
    public void removeFromCart(String userEmail, Long productId) {
        if (!userRepo.existsByEmail(userEmail)) {
            throw new UserNotFoundException("User with email " + userEmail + " not found");
        }
        CartItem item = cartRepo.findByUserEmailAndProductId(userEmail, productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found in the cart"));
        cartRepo.delete(item);
    }
}