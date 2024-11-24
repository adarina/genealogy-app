package com.ada.genealogyapp.event.validation;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.user.validation.ValidationResult;

import static java.util.Objects.nonNull;

public abstract class EventValidator {

    private EventValidator next;

    public static EventValidator link(EventValidator first, EventValidator... chain) {
        EventValidator head = first;
        for (EventValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void check(Event event, ValidationResult result);

    protected void checkNext(Event event, ValidationResult result) {
        if (nonNull(next)) {
            next.check(event, result);
        }
    }
}