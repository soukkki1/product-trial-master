package com.ecomm_alten.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyInWishListException extends RuntimeException {
    public AlreadyInWishListException(Long productId) {
        super("Product with ID " + productId + " is already in the wishlist");
    }
}
