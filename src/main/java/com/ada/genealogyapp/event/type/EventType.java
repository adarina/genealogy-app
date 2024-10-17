package com.ada.genealogyapp.event.type;

import lombok.Getter;

@Getter
public enum EventType {

    BIRTH("Person"),
    BAPTISM("Person"),
    MARRIAGE("Family"),
    DIVORCE("Family");

    private final String applicableTo;

    EventType(String applicableTo) {
        this.applicableTo = applicableTo;
    }

}
