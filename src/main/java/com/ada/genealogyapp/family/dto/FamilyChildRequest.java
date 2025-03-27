package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FamilyChildRequest extends PersonRequest {

    private PersonRelationshipType motherRelationship;

    private PersonRelationshipType fatherRelationship;

}