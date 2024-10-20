package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class RollbackException extends RuntimeException {
    public RollbackException(String message) {
        super(message);
    }
}
