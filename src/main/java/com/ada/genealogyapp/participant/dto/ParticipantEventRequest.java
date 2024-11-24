package com.ada.genealogyapp.participant.dto;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ParticipantEventRequest extends EventRequest {

    public EventParticipantRelationshipType relationship;

}