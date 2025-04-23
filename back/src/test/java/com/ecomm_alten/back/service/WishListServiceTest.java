package com.ecomm_alten.back.service;


import com.ecomm_alten.back.exception.AlreadyInWishListException;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.model.WishListItem;
import com.ecomm_alten.back.repository.ProductRepository;
import com.ecomm_alten.back.repository.UserRepository;
import com.ecomm_alten.back.repository.WishListItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class)
class WishListServiceTest {

    @Mock WishListItemRepository wishRepo;
    @Mock UserRepository userRepo;
    @Mock ProductRepository prodRepo;
    @InjectMocks
    WishListService svc;

    @Test
    void getWishList_ReturnsList() {
        when(wishRepo.findByUserEmail("u")).thenReturn(List.of());
        assertEquals(0, svc.getWishList("u").size());
    }

    @Test
    void addToWishList_Success() {
        User u = new User(); u.setEmail("u");
        Product p = new Product(); p.setId(5L); p.setName("X");
        when(userRepo.findByEmail("u")).thenReturn(Optional.of(u));
        when(prodRepo.findById(5L)).thenReturn(Optional.of(p));
        when(wishRepo.findByUserEmailAndProductId("u",5L))
                .thenReturn(Optional.empty());

        WishListItem saved = new WishListItem();
        when(wishRepo.save(any())).thenReturn(saved);

        var result = svc.addToWishList("u",5L);
        assertSame(saved, result);
        verify(wishRepo).save(any());
    }

    @Test
    void addToWishList_AlreadyExists() {
        User u = new User(); u.setEmail("u");
        Product p = new Product(); p.setId(5L);
        when(userRepo.findByEmail("u")).thenReturn(Optional.of(u));
        when(prodRepo.findById(5L)).thenReturn(Optional.of(p));
        when(wishRepo.findByUserEmailAndProductId("u",5L))
                .thenReturn(Optional.of(new WishListItem()));

        assertThrows(AlreadyInWishListException.class, () ->
                svc.addToWishList("u",5L)
        );
        verify(wishRepo, never()).save(any());
    }

    @Test
    void removeFromWishList_Success() {
        WishListItem item = new WishListItem();
        when(wishRepo.findByUserEmailAndProductId("u",5L))
                .thenReturn(Optional.of(item));

        svc.removeFromWishList("u",5L);
        verify(wishRepo).delete(item);
    }
}

