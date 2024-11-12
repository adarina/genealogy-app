package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonFamiliesResponse {


    private UUID id;

    private String fatherName;

    private UUID fatherId;

    private LocalDate fatherBirthdate;

    private String motherName;

    private UUID motherId;

    private LocalDate motherBirthdate;

    private LinkedHashSet<PersonFamiliesChildResponse> children;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonFamiliesChildResponse {

        private UUID childId;

        private String childName;

        private LocalDate childBirthdate;
    }
}
