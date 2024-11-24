package com.ada.genealogyapp.family.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FamilyFilterRequest {

    private String motherName;

    private String fatherName;

    private String status;
}
