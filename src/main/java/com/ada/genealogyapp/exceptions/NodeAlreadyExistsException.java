package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class NodeAlreadyExistsException extends RuntimeException {
    public NodeAlreadyExistsException(String message) {
        super(message);
    }
}
