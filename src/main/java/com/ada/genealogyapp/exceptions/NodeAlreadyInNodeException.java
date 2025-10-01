package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class NodeAlreadyInNodeException extends Exception {
    public NodeAlreadyInNodeException(String message) {
        super(message);
    }
}
