package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventParticipantResponse {

    private String id;

    private String name;

    private EventParticipantRelationshipType relationship;

}