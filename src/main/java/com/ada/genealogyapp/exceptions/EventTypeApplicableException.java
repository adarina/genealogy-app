package com.ada.genealogyapp.exceptions;

import lombok.Getter;

@Getter
public class EventTypeApplicableException extends Exception {
    public EventTypeApplicableException(String message) {
        super(message);
    }
}