package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PersonUpdateRequest {

    public UUID id;

    public EventParticipantRelationshipType eventParticipantRelationshipType;

}