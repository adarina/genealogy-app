package com.ada.genealogyapp.user.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UserRequest {

    private String firstname;

    private String lastname;

    private String username;

    private String phone;

    private String password;

    private String role;
}