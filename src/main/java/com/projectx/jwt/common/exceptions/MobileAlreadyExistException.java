package com.projectx.jwt.common.exceptions;

public class MobileAlreadyExistException extends RuntimeException{
    public MobileAlreadyExistException(String message) {
        super(message);
    }
}
