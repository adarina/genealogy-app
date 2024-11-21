package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PersonRequest {

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    private GenderType gender;

    private PersonRelationshipType motherRelationship;

    private PersonRelationshipType fatherRelationship;

}