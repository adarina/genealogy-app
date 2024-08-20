package com.ada.genealogyapp.user.dto;

import com.ada.genealogyapp.user.model.User;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateUserRequest {

    private String firstname;

    private String lastname;

    private String username;

    private String phone;

    private String password;

    private String role;

    public static Function<CreateUserRequest, User> dtoToEntityMapper() {
        return request -> User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .phone(request.getPhone())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }
}