package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException{
    public ExternalApiException(String message) {
        super(message);
    }
}
