package com.ada.genealogyapp.participant.dto;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParticipantEventRequest extends EventRequest {

    public EventParticipantRelationshipType relationship;

}