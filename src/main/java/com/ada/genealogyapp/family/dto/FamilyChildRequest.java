package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyChildRequest {

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    private GenderType gender;

    private PersonRelationshipType motherRelationship;

    private PersonRelationshipType fatherRelationship;

}
