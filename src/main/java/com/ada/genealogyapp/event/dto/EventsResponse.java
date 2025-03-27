package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventsResponse {

    private String id;

    private String description;

    private String date;

    private String place;

    private EventType type;

    private String participantNames;

}