package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.type.EventRelationshipType;
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

    public EventRelationshipType eventRelationshipType;

}