package com.ada.genealogyapp.user.dto;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUsersResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class User {

        private Long id;

        private String username;

        private String password;

        private String role;

    }

    @Singular
    private List<User> users;

    public static Function<Collection<com.ada.genealogyapp.user.model.User>,
            GetUsersResponse> entityToDtoMapper() {
        return users -> {
            GetUsersResponseBuilder response = GetUsersResponse.builder();
            users.stream()
                    .map(user -> User.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .role(user.getRole())
                            .build())
                    .forEach(response::user);
            return response.build();
        };
    }
}
