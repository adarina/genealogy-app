package com.ada.genealogyapp.family.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FamilyResponse extends FamiliesResponse {
    private String fatherBirthdate;
    private String fatherDeathdate;
    private String motherBirthdate;
    private String motherDeathdate;
}