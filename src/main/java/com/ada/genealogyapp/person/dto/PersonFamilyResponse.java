package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.util.LinkedHashSet;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonFamilyResponse {

    private String id;

    private String fatherName;

    private String fatherId;

    private String fatherBirthdate;

    private String fatherDeathdate;

    private String motherName;

    private String motherId;

    private String motherBirthdate;

    private String motherDeathdate;

    private LinkedHashSet<PersonFamilyChildResponse> children;
}