package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventType;
import lombok.*;

import java.util.LinkedHashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {

    private String id;

    private EventType type;

    private String description;

    private String date;

    private String place;

    private LinkedHashSet<EventParticipantResponse> participants;

    private LinkedHashSet<EventCitationResponse> citations;

}