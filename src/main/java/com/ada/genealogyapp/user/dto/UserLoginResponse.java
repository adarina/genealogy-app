package com.ada.genealogyapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {

    private String username;

    private String accessToken;

    private Long id;

    private String role;
}
