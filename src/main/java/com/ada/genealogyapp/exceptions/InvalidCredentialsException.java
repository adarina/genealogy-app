package com.ada.genealogyapp.exceptions;


import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
