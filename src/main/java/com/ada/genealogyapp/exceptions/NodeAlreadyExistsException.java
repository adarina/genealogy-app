package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class NodeAlreadyExistsException extends Exception {
    public NodeAlreadyExistsException(String message) {
        super(message);
    }
}
