package com.ada.genealogyapp.location.dto;

import com.ada.genealogyapp.location.type.LocationType;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class LocationRequest {

    private String name;

    private LocationType type;

    private Double latitude;

    private Double longitude;

    private Boolean isMain;
}
