package com.ada.genealogyapp.event.mapper.export.json;

import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.dto.EventParticipantRequest;
import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.export.json.JsonMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EventJsonMapper implements JsonMapper<Event, EventJsonRequest> {

    @Override
    public EventJsonRequest map(Event event) {
        return EventJsonRequest.builder()
                .id(event.getId())
                .type(event.getType())
                .date(event.getDate())
                .place(event.getPlace())
                .description(event.getDescription())
                .participants(
                        event.getParticipants().stream()
                                .map(relationship -> EventParticipantRequest.builder()
                                        .id(relationship.getId())
                                        .participantId(relationship.getParticipant().getId())
                                        .relationship(relationship.getRelationship())
                                        .build())
                                .collect(Collectors.toList())
                )
                .citations(
                        event.getCitations().stream()
                                .map(relationship -> EventCitationRequest.builder()
                                        .id(relationship.getId())
                                        .citationId(relationship.getCitation().getId())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public Class<Event> getEntityType() {
        return Event.class;
    }
}

