package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilyEventsResponse {

    private UUID id;

    private EventParticipantRelationshipType relationship;

    private EventType type;

    private String description;

    private LocalDate date;

    private String place;

    private List<FamilyEventsParticipantResponse> participants;

    private List<FamilyEventsCitationResponse> citations;

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class FamilyEventsParticipantResponse {

        private String name;

    }

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class FamilyEventsCitationResponse {

        private String id;

    }
}
