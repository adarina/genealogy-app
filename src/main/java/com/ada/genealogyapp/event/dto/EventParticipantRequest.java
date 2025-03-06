package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipantRequest {

    private String id;

    private String participantId;

    private EventParticipantRelationshipType relationship;
}
