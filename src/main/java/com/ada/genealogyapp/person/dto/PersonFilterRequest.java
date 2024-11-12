package com.ada.genealogyapp.person.dto;

import lombok.Getter;

@Getter
public class PersonFilterRequest {

    private String firstname;

    private String lastname;

    private String gender;
}
