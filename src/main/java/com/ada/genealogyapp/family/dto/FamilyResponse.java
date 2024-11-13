package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.type.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyResponse {

    private UUID id;

    private StatusType status;

    private String motherName;

    private String fatherName;

    private LocalDate fatherBirthdate;

    private LocalDate motherBirthdate;
}
