package com.ada.genealogyapp.exceptions;

import lombok.Getter;


@Getter
public class StorageFileNotFoundException extends Exception {
    public StorageFileNotFoundException(String message) {
        super(message);
    }
}

