package com.ada.genealogyapp.person.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonFamilyChildResponse {

    private String childId;

    private String childName;

    private String childBirthdate;

    private String childDeathdate;
}
