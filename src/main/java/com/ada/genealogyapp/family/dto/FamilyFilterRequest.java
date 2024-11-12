package com.ada.genealogyapp.family.dto;


import lombok.Getter;

@Getter
public class FamilyFilterRequest {

    private String motherName;

    private String fatherName;

    private String status;
}
