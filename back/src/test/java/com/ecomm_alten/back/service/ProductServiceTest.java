package com.ecomm_alten.back.service;

import com.ecomm_alten.back.exception.ProductNotFoundException;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repo;
    @InjectMocks
    ProductService svc;


    @Test
    void createProduct_Saves() {
        Product p = new Product();
        p.setCode("P-001");
        p.setName("Test name");
        p.setDescription("Test description");
        p.setPrice(5);
        p.setQuantity(5);
        when(repo.save(p)).thenReturn(p);

        var result = svc.createProduct(p);
        assertSame(p, result);
        verify(repo).save(p);
    }

    @Test
    void getById_Found() {
        Product p = new Product();
        p.setCode("P-001");
        p.setName("Test name");
        p.setDescription("Test description");
        p.setPrice(5);
        p.setQuantity(5);
        when(repo.findById(1L)).thenReturn(Optional.of(p));
        assertEquals(p, svc.getProductById(1L));
    }

    @Test
    void getById_NotFound_Throws() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () ->
                svc.getProductById(1L)
        );
    }

    @Test
    void deleteProduct_Existing() {
        when(repo.existsById(1L)).thenReturn(true);
        assertTrue(svc.deleteProduct(1L));
        verify(repo).deleteById(1L);
    }

    @Test
    void deleteProduct_NotExisting() {
        when(repo.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> svc.deleteProduct(1L));
    }
}

