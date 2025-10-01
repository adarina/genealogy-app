package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
