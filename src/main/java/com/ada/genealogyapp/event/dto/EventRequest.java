package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
//import com.ada.genealogyapp.person.dto.PersonEventRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    private EventType type;

    private LocalDate date;

    private String place;

    private String description;

    private EventParticipantRelationshipType relationship;

    private List<EventParticipantRequest> participants;

    private List<EventCitationRequest> citations;

    @Getter
    @AllArgsConstructor
    public static class EventParticipantRequest {

        private UUID id;

        private String name;

    }

    @Getter
    @AllArgsConstructor
    public static class EventCitationRequest {

        private UUID id;

    }
}