package com.github.rodrigofcr.wishlist.api.exception;

public class ApiException extends RuntimeException {
    private final String message;


    public ApiException(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
