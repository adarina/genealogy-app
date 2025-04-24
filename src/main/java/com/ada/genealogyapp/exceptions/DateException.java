package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class DateException extends RuntimeException {
    public DateException(String message) {
        super(message);
    }
}

