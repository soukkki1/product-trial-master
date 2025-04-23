package com.ecomm_alten.back.service;

import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.model.WishListItem;
import com.ecomm_alten.back.repository.ProductRepository;
import com.ecomm_alten.back.repository.UserRepository;
import com.ecomm_alten.back.repository.WishListItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class WishListService {
    private final WishListItemRepository wishRepo;
    private final UserRepository userRepo;
    private final ProductRepository prodRepo;

    public List<WishListItem> getWishList(String userEmail) {
        log.info("Fetching wishlist for user: {}", userEmail);
        return wishRepo.findByUserEmail(userEmail);
    }

    @Transactional
    public WishListItem addToWishList(String userEmail, Long productId) {
        log.info("Adding product with ID: {} to wishlist for user: {}", productId, userEmail);
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> {
            log.error("User with email: {} not found", userEmail);
            return new IllegalArgumentException("User not found");
        });
        Product p = prodRepo.findById(productId).orElseThrow(() -> {
            log.error("Product with ID: {} not found", productId);
            return new IllegalArgumentException("Product not found");
        });

        if (wishRepo.findByUserEmailAndProductId(userEmail, productId).isPresent()) {
            log.warn("Product with ID: {} already exists in wishlist for user: {}", productId, userEmail);
            throw new IllegalStateException("Déjà en liste d'envie");
        }
        WishListItem item = new WishListItem();
        item.setUser(user);
        item.setProduct(p);
        WishListItem savedItem = wishRepo.save(item);
        log.info("Product with ID: {} added to wishlist for user: {}", productId, userEmail);
        return savedItem;
    }

    @Transactional
    public void removeFromWishList(String userEmail, Long productId) {
        log.info("Removing product with ID: {} from wishlist for user: {}", productId, userEmail);
        wishRepo.findByUserEmailAndProductId(userEmail, productId)
                .ifPresentOrElse(item -> {
                    wishRepo.delete(item);
                    log.info("Product with ID: {} removed from wishlist for user: {}", productId, userEmail);
                }, () -> {
                    log.warn("Product with ID: {} not found in wishlist for user: {}", productId, userEmail);
                });
    }
}
