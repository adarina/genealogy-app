package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyChildResponse extends PersonResponse {

    private PersonRelationshipType fatherRelationship;

    private PersonRelationshipType motherRelationship;

}