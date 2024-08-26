package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
