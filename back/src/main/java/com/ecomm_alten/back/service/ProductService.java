package com.ecomm_alten.back.service;

import com.ecomm_alten.back.exception.ProductNotFoundException;
import com.ecomm_alten.back.model.Product;
import com.ecomm_alten.back.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(Product product) {
        product.setCreatedAt(System.currentTimeMillis());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setUpdatedAt(System.currentTimeMillis());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }


    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id " + id);
        }
            productRepository.deleteById(id);
            return true;
    }
}