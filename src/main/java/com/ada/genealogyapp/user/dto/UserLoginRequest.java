package com.ada.genealogyapp.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {

    private String username;

    private String password;
}