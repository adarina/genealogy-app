package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.*;

import java.time.LocalDate;
import java.util.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonEventsResponse {

    private UUID id;

    private EventParticipantRelationshipType relationship;

    private EventType type;

    private String description;

    private LocalDate date;

    private String place;

    private List<PersonEventsParticipantResponse> participants;

    private List<PersonEventsCitationResponse> citations;

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class PersonEventsParticipantResponse {

        private String name;

    }

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class PersonEventsCitationResponse {

        private String id;

    }
}