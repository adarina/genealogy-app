package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventRelationshipType;
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

    private EventRelationshipType eventRelationshipType;

    public static Function<EventRequest, Event> dtoToEntityMapper() {
        return request -> Event.builder()
                .eventType(request.getType())
                .date(request.getDate())
                .place(request.getPlace())
                .description(request.getDescription())
                .build();
    }
}