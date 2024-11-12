package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonEventRequest {

    private EventType type;

    private LocalDate date;

    private String place;

    private String description;

    private EventParticipantRelationshipType relationship;

    private List<Participant> participants;

    private List<Citation> citations;

    @Getter
    @AllArgsConstructor
    public static class Participant {

        private UUID id;

        private String name;

    }

    @Getter
    @AllArgsConstructor
    public static class Citation {

        private UUID id;

    }

    public static Function<EventRequest, Event> dtoToEntityMapper() {
        return request -> Event.builder()
                .type(request.getType())
                .date(request.getDate())
                .place(request.getPlace())
                .description(request.getDescription())
                .build();
    }
}
