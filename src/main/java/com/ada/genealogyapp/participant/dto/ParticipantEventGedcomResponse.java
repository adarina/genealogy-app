package com.ada.genealogyapp.participant.dto;

import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantEventGedcomResponse {

    private String id;

    private EventType type;

    private String description;

    private String date;

    private String place;

    private List<EventCitationResponse> citations;

}
