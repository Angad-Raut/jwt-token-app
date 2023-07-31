package com.projectx.jwt.common.exceptions;

public class TokenExpiryedException extends RuntimeException{
    public TokenExpiryedException(String message) {
        super(message);
    }
}
