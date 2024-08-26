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
public class UserResponse {

    private String username;

    public static Function<User, UserResponse> entityToDtoMapper() {
        return user -> UserResponse.builder()
                .username(user.getUsername())
                .build();
    }
}