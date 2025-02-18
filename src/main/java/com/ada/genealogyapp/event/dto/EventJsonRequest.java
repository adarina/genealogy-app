package com.ada.genealogyapp.event.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventJsonRequest extends EventRequest {

    private String id;

    List<EventParticipantRequest> participants;

    List<EventCitationRequest> citations;
}
