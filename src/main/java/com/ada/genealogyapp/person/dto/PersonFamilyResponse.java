package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonFamilyResponse {


    private UUID id;

    private String fatherName;

    private UUID fatherId;

    private LocalDate fatherBirthdate;

    private String motherName;

    private UUID motherId;

    private LocalDate motherBirthdate;

    private LinkedHashSet<PersonFamilyChildResponse> children;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonFamilyChildResponse {

        private UUID childId;

        private String childName;

        private LocalDate childBirthdate;
    }
}
