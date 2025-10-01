package com.ada.genealogyapp.gedcom.mappers;

import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.gedcom.type.EventGedcomType;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;


public class EventMapper {

    private static final Map<EventType, EventGedcomType> eventTypeToGedcomTagMap = new HashMap<>();
    private static final Map<EventGedcomType, EventType> gedcomTagToEventTypeMap = new HashMap<>();

    static {
        eventTypeToGedcomTagMap.put(EventType.BIRTH, EventGedcomType.BIRT);
        eventTypeToGedcomTagMap.put(EventType.DEATH, EventGedcomType.DEAT);
        eventTypeToGedcomTagMap.put(EventType.CHRISTENING, EventGedcomType.CHR);
        eventTypeToGedcomTagMap.put(EventType.MARRIAGE, EventGedcomType.MARR);
        eventTypeToGedcomTagMap.put(EventType.MARRIAGE_BANN, EventGedcomType.MARB);
        eventTypeToGedcomTagMap.put(EventType.DIVORCE, EventGedcomType.DIV);
        eventTypeToGedcomTagMap.put(EventType.BURIAL, EventGedcomType.BURI);
        eventTypeToGedcomTagMap.put(EventType.EMIGRATION, EventGedcomType.EMIG);
        eventTypeToGedcomTagMap.put(EventType.IMMIGRATION, EventGedcomType.IMMI);
        eventTypeToGedcomTagMap.put(EventType.MILITARY, EventGedcomType._MILT);
        eventTypeToGedcomTagMap.put(EventType.CENSUS, EventGedcomType.CENS);
        eventTypeToGedcomTagMap.put(EventType.NATURALIZATION, EventGedcomType.NATU);


        gedcomTagToEventTypeMap.put(EventGedcomType.BIRT, EventType.BIRTH);
        gedcomTagToEventTypeMap.put(EventGedcomType.DEAT, EventType.DEATH);
        gedcomTagToEventTypeMap.put(EventGedcomType.CHR, EventType.CHRISTENING);
        gedcomTagToEventTypeMap.put(EventGedcomType.MARR, EventType.MARRIAGE);
        gedcomTagToEventTypeMap.put(EventGedcomType.MARB, EventType.MARRIAGE_BANN);
        gedcomTagToEventTypeMap.put(EventGedcomType.DIV, EventType.DIVORCE);
        gedcomTagToEventTypeMap.put(EventGedcomType.BURI, EventType.BURIAL);
        gedcomTagToEventTypeMap.put(EventGedcomType.EMIG, EventType.EMIGRATION);
        gedcomTagToEventTypeMap.put(EventGedcomType.IMMI, EventType.IMMIGRATION);
        gedcomTagToEventTypeMap.put(EventGedcomType._MILT, EventType.MILITARY);
        gedcomTagToEventTypeMap.put(EventGedcomType.CENS, EventType.CENSUS);
        gedcomTagToEventTypeMap.put(EventGedcomType.NATU, EventType.NATURALIZATION);
        gedcomTagToEventTypeMap.put(EventGedcomType.EVEN, EventType.EVENT);



    }

    public static EventGedcomType getGedcomEventTag(EventType eventType) {
        return eventTypeToGedcomTagMap.get(eventType);
    }

    public static EventType mapEvent(EventGedcomType gedcomType) {
        if (isNull(gedcomType)) {
            return EventType.ERROR;
        }
        return gedcomTagToEventTypeMap.getOrDefault(gedcomType, EventType.ERROR);
    }
}
