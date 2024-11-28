package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import lombok.*;

import java.time.LocalDate;

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

    private LocalDate birthdate;

    private GenderType gender;

}