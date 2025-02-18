package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.time.LocalDate;
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

    private String motherName;

    private String motherId;

    private String motherBirthdate;

    private LinkedHashSet<PersonFamilyChildResponse> children;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonFamilyChildResponse {

        private String childId;

        private String childName;

        private String childBirthdate;
    }
}
