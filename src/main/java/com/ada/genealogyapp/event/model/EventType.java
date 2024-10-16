package com.ada.genealogyapp.event.model;

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
