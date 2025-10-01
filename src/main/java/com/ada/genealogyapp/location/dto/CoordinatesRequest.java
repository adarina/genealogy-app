package com.ada.genealogyapp.location.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CoordinatesRequest {

    private String location;

    private String state;

    private String country;
}
