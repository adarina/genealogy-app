package com.ada.genealogyapp.person.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonFilterRequest {

    private String firstname;

    private String lastname;

    private String gender;
}
