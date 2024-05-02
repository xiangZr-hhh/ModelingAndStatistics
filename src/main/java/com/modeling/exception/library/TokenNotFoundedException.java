package com.modeling.exception.library;

public class TokenNotFoundedException extends RuntimeException {
    public TokenNotFoundedException(String message) {
        super(message);
    }
}
