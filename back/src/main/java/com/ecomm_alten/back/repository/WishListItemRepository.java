package com.ecomm_alten.back.repository;

import com.ecomm_alten.back.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {
    List<WishListItem> findByUserEmail(String email);
    Optional<WishListItem> findByUserEmailAndProductId(String email, Long productId);
}
