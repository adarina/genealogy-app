package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {

    private UUID id;

    private EventType type;

    private String description;

    private LocalDate date;

    private String place;

    private LinkedHashSet<EventParticipantResponse> participants;

    private LinkedHashSet<EventCitationResponse> citations;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventParticipantResponse {

        private UUID id;

        private String name;

        private EventParticipantRelationshipType relationship;

    }


}