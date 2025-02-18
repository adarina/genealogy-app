package com.ada.genealogyapp.participant.dto;

import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantEventResponse {

    private String id;

    private EventType type;

    private String description;

    private String date;

    private String place;

    private EventParticipantRelationshipType relationship;

    private List<EventParticipantResponse> participants;

    private List<EventCitationResponse> citations;

}