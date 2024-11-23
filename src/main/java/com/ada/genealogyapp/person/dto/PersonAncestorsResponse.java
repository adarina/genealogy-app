package com.ada.genealogyapp.person.dto;

import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonAncestorsResponse {

    private UUID id;

    private String name;

    private String gender;

    private String birthdate;

    private List<PersonAncestorsResponse> ancestors;

}