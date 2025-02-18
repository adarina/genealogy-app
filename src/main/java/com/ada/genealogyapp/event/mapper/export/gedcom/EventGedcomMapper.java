package com.ada.genealogyapp.event.mapper.export.gedcom;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventGedcomMapper extends GedcomMapper<Event> {

    @Override
    public Class<Event> getEntityType() {
        return Event.class;
    }


    @Override
    public String generateGedcomContent(Event event, String gedcomId, Map<String, String> personGedcomIds) {
        StringBuilder builder = new StringBuilder();
        builder.append("0 ").append(gedcomId).append(" EVEN\n")
                .append("1 TYPE ").append(event.getType().name()).append("\n")
                .append("1 DATE ").append(event.getDate()).append("\n")
                .append("1 PLAC ").append(event.getPlace()).append("\n");
        return builder.toString();
    }

    @Override
    public String getEntityId(Event event) {
        return event.getId();
    }

    @Override
    public String getEntityTypePrefix() {
        return "E";
    }
}

