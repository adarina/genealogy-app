package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class EventResponse {

    private String id;

    private EventType type;

    private String description;

    private String date;

    private String place;

}
