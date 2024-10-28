package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.relationship.EventRelationship;
import lombok.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class PersonEventsResponse {

    @Getter
    @Builder
    public static class Event {

        private UUID id;

        private String type;

        private String description;

        private String date;

        private String place;

        private String eventRelationshipType;

        private List<ParticipantInfo> participants;

        private List<CitationInfo> citations;

        @Getter
        @AllArgsConstructor
        public static class ParticipantInfo {

            private UUID id;

            private String name;

        }

        @Getter
        @AllArgsConstructor
        public static class CitationInfo {

            private UUID id;

        }
    }

    @Singular
    private List<Event> events;

    public static Function<Collection<EventRelationship>, PersonEventsResponse> entityToDtoMapper() {
        return eventRelationships -> {
            PersonEventsResponseBuilder response = PersonEventsResponse.builder();
            eventRelationships.stream()
                    .map(eventRelationship -> {
                        com.ada.genealogyapp.event.model.Event event = eventRelationship.getEvent();
                        return Event.builder()
                                .id(event.getId())
                                .type(event.getEventType().name())
                                .description(event.getDescription())
                                .date(event.getDate().toString())
                                .place(event.getPlace())
                                .eventRelationshipType(eventRelationship.getEventRelationshipType().toString())
                                .participants(event.getParticipants().stream()
                                        .map(participant -> new Event.ParticipantInfo(
                                                participant.getParticipantId(),
                                                participant.getParticipantName()
                                        ))
                                        .collect(Collectors.toList()))
                                .citations(event.getCitations().stream()
                                        .map(citation -> new Event.CitationInfo(citation.getId()))
                                        .collect(Collectors.toList()))
                                .build();
                    })
                    .forEach(response::event);
            return response.build();
        };
    }
}
