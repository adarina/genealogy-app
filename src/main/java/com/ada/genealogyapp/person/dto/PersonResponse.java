package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    private String id;

    private String firstname;

    private String lastname;

    private String name;

    private String birthdate;

    private String deathdate;

    private GenderType gender;

}