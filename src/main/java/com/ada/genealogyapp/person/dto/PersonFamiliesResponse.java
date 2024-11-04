package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Builder
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

    private List<Child> children;


    @Getter
    @AllArgsConstructor
    public static class Child {

        private UUID childId;

        private String childName;

        private LocalDate childBirthdate;
    }
}
