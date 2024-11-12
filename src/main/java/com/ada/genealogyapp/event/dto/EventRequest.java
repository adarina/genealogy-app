package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.*;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class EventRequest {

    private EventType type;

    private LocalDate date;

    private String place;

    private String description;

    private EventParticipantRelationshipType relationship;

    public static Function<EventRequest, Event> dtoToEntityMapper() {
        return request -> Event.builder()
                .type(request.getType())
                .date(request.getDate())
                .place(request.getPlace())
                .description(request.getDescription())
                .build();
    }
}