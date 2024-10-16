package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class EventTypeApplicableException extends RuntimeException {
    public EventTypeApplicableException(String message) {
        super(message);
    }
}