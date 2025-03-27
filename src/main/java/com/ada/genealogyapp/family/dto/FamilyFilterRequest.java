package com.ada.genealogyapp.family.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyFilterRequest {

    private String motherName;

    private String fatherName;

    private String status;
}
