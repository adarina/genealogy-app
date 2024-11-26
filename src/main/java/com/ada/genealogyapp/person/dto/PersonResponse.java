package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    private UUID id;

    private String firstname;

    private String lastname;

    private String name;

    private LocalDate birthdate;

    private GenderType gender;

}