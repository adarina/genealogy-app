package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class NodeAlreadyInNodeException extends RuntimeException {
    public NodeAlreadyInNodeException(String message) {
        super(message);
    }
}
