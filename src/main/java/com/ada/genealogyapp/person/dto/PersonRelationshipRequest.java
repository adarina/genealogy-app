package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRelationshipRequest {

    private String id;

    private String childId;

    private PersonRelationshipType relationship;
}