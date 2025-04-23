package com.ecomm_alten.back.service;

import com.ecomm_alten.back.exception.ProductNotFoundException;
import com.ecomm_alten.back.exception.UserNotFoundException;
import com.ecomm_alten.back.model.CartItem;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.repository.CartItemRepository;
import com.ecomm_alten.back.repository.ProductRepository;
import com.ecomm_alten.back.repository.UserRepository;
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
class CartServiceTest {

    @Mock
    CartItemRepository cartRepo;
    @Mock
    UserRepository userRepo;
    @Mock
    ProductRepository prodRepo;
    @InjectMocks
    CartService svc;

    @Test
    void getCart_ReturnsList() {
        when(userRepo.existsByEmail("u")).thenReturn(true);
        when(cartRepo.findByUserEmail("u")).thenReturn(List.of());
        assertEquals(0, svc.getCart("u").size());
    }

    @Test
    void addToCart_NewItem() {
        User u = new User(); u.setEmail("u");
        Product p = new Product(); p.setId(2L);
        when(userRepo.findByEmail("u")).thenReturn(Optional.of(u));
        when(prodRepo.findById(2L)).thenReturn(Optional.of(p));
        when(cartRepo.findByUserEmailAndProductId("u",2L))
                .thenReturn(Optional.empty());
        CartItem saved = new CartItem();
        when(cartRepo.save(any())).thenReturn(saved);

        var result = svc.addToCart("u",2L,3);
        assertSame(saved, result);
        verify(cartRepo).save(any());
    }

    @Test
    void addToCart_ExistingItem_Increments() {
        User u = new User();
        u.setEmail("u");
        when(userRepo.findByEmail("u")).thenReturn(Optional.of(u));
        Product p = new Product();
        p.setId(2L);
        when(prodRepo.findById(2L))
                .thenReturn(Optional.of(p));
        CartItem existing = new CartItem();
        existing.setQuantity(1);
        when(cartRepo.findByUserEmailAndProductId("u",2L))
                .thenReturn(Optional.of(existing));
        when(cartRepo.save(existing)).thenReturn(existing);
        var result = svc.addToCart("u",2L,2);
        assertEquals(3, result.getQuantity());
        verify(cartRepo).save(existing);
    }

    @Test
    void removeFromCart_Success() {
        CartItem item = new CartItem();
        when(userRepo.existsByEmail("u")).thenReturn(true);
        when(cartRepo.findByUserEmailAndProductId("u",2L))
                .thenReturn(Optional.of(item));

        svc.removeFromCart("u",2L);
        verify(cartRepo).delete(item);
    }

    @Test
    void removeFromCart_UserNotFound() {
        when(userRepo.existsByEmail("u")).thenReturn(false);
        assertThrows(UserNotFoundException.class, () ->
                svc.removeFromCart("u",2L)
        );
    }

    @Test
    void removeFromCart_ProductNotInCart() {
        when(userRepo.existsByEmail("u")).thenReturn(true);
        when(cartRepo.findByUserEmailAndProductId("u",2L))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                svc.removeFromCart("u",2L)
        );
    }
}

