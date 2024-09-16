package com.ada.genealogyapp.exceptions;


import lombok.Getter;

@Getter
public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(String message) {
        super(message);
    }
}
