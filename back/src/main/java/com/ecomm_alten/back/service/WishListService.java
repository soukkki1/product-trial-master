package com.ecomm_alten.back.service;

import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.model.WishListItem;
import com.ecomm_alten.back.repository.ProductRepository;
import com.ecomm_alten.back.repository.UserRepository;
import com.ecomm_alten.back.repository.WishListItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WishListService {
    private final WishListItemRepository wishRepo;
    private final UserRepository userRepo;
    private final ProductRepository prodRepo;

    public List<WishListItem> getWishList(String userEmail) {
        return wishRepo.findByUserEmail(userEmail);
    }

    @Transactional
    public WishListItem addToWishList(String userEmail, Long productId) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        Product p = prodRepo.findById(productId).orElseThrow();

        if (wishRepo.findByUserEmailAndProductId(userEmail, productId).isPresent()) {
            throw new IllegalStateException("Déjà en liste d'envie");
        }
        WishListItem item = new WishListItem();
        item.setUser(user);
        item.setProduct(p);
        return wishRepo.save(item);
    }

    @Transactional
    public void removeFromWishList(String userEmail, Long productId) {
        wishRepo.findByUserEmailAndProductId(userEmail, productId)
                .ifPresent(wishRepo::delete);
    }
}
