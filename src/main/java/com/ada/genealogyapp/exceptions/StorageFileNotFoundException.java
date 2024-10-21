package com.ada.genealogyapp.exceptions;

import lombok.Getter;


@Getter
public class StorageFileNotFoundException extends RuntimeException {
    public StorageFileNotFoundException(String message) {
        super(message);
    }
}

