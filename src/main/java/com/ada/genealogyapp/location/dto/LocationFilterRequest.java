package com.ada.genealogyapp.location.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationFilterRequest {

    String name;

    String type;
}
