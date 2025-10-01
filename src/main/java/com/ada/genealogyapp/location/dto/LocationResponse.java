package com.ada.genealogyapp.location.dto;

import com.ada.genealogyapp.location.type.LocationType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class LocationResponse {

    private String id;

    private String name;

    private LocationType type;

    private Double latitude;

    private Double longitude;

}


