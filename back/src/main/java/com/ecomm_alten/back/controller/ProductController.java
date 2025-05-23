package com.ecomm_alten.back.controller;

import com.ecomm_alten.back.dto.ProductRequestDto;
import com.ecomm_alten.back.dto.ProductResponseDto;
import com.ecomm_alten.back.mapper.ProductMapper;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        var product = productMapper.toEntity(dto);
        var newProduct = productService.createProduct(product);
        return ResponseEntity.ok(productMapper.toDto(newProduct));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        Page<ProductResponseDto> productResponseDtos = products.map(productMapper::toDto);
        return ResponseEntity.ok(productResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(productMapper.toDto(product));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto productDetails) {
        Product updatedProduct = productService.updateProduct(id, productMapper.toEntity(productDetails));
        if (updatedProduct != null) {
            return ResponseEntity.ok(productMapper.toDto(updatedProduct));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}