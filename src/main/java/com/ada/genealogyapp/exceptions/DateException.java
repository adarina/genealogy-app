package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class DateException extends Exception {
    public DateException(String message) {
        super(message);
    }
}

