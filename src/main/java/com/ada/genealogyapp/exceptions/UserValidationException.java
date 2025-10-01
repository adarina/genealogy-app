package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class UserValidationException extends Exception {
    public UserValidationException(String message) {
        super(message);
    }
}
