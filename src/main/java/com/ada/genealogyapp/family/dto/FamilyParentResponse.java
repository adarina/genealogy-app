package com.ada.genealogyapp.family.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FamilyParentResponse {

    private UUID id;

    private String firstname;

    private String lastname;

    private LocalDate birthDate;
}