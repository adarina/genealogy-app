package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonEventResponse {

    private UUID id;

    private EventType type;

    private String description;

    private LocalDate date;

    private String place;

    private EventParticipantRelationshipType relationship;

    private List<PersonEventParticipantResponse> participants;

    private List<PersonEventCitationResponse> citations;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonEventParticipantResponse {

        private UUID id;

        private String name;

        private EventParticipantRelationshipType relationship;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonEventCitationResponse {

        private UUID id;

        private String page;

    }
}