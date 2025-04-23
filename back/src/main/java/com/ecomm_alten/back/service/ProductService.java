package com.ecomm_alten.back.service;

import com.ecomm_alten.back.exception.ProductNotFoundException;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(Product product) {
        log.info("Creating a new product with details: {}", product);
        product.setCreatedAt(System.currentTimeMillis());
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products", products.size());
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new ProductNotFoundException("Product not found with id " + id);
                });
        log.info("Fetched product details: {}", product);
        return product;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(Long id, Product productDetails) {
        log.info("Updating product with ID: {}", id);
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setUpdatedAt(System.currentTimeMillis());
            Product updatedProduct = productRepository.save(existingProduct);
            log.info("Product with ID {} updated successfully", id);
            return updatedProduct;
        }).orElseThrow(() -> {
            log.error("Product with ID {} not found", id);
            return new ProductNotFoundException("Product not found with id " + id);
        });
    }

    public boolean deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.error("Product with ID {} not found", id);
            throw new ProductNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
        log.info("Product with ID {} deleted successfully", id);
        return true;
    }
}