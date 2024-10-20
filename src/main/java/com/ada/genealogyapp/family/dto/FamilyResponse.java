package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.type.FamilyRelationshipType;
import com.ada.genealogyapp.person.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyResponse {

    private UUID id;

    private String fatherFirstname;

    private String fatherLastname;

    private LocalDate fatherBirthDate;

    private String motherFirstname;

    private String motherLastname;

    private LocalDate motherBirthDate;

    private FamilyRelationshipType familyRelationshipType;

}
