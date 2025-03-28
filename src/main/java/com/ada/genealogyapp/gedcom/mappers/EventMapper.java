package com.ada.genealogyapp.gedcom.mappers;

import com.ada.genealogyapp.event.type.EventType;

public class EventMapper {

    public static EventType mapEvent(String value) {
        if (value == null) {
            return EventType.ERROR;
        }
        return switch (value.toUpperCase()) {
            case "BIRT" -> EventType.BIRTH;
            case "DEAT" -> EventType.DEATH;
            case "CHR" -> EventType.CHRISTENING;
            case "MARR" -> EventType.MARRIAGE;
            case "MARB" -> EventType.MARRIAGE_BANN;
            case "DIV" -> EventType.DIVORCE;
            case "BURI" -> EventType.BURIAL;
            default -> EventType.ERROR;
        };
    }
}
