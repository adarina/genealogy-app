package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilyChildrenResponse {

    private UUID id;

    private String name;

    private LocalDate birthdate;

    private String gender;

    private PersonRelationshipType fatherRelationship;

    private PersonRelationshipType motherRelationship;

}
