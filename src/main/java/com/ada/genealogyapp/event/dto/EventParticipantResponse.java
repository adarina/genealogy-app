package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantResponse {

    private UUID id;

    private String name;

    private EventParticipantRelationshipType relationship;

}
