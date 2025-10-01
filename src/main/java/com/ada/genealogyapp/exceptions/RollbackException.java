package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class RollbackException extends Exception {
    public RollbackException(String message) {
        super(message);
    }
}
