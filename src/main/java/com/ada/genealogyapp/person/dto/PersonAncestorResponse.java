package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonAncestorResponse {

    private String id;

    private String name;

    private String gender;

    private String birthdate;

    private String deathdate;

    private List<PersonAncestorResponse> ancestors;
}