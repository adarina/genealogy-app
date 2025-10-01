package com.ada.genealogyapp.location.dto;

import com.ada.genealogyapp.location.type.LocationType;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationChildResponse {

    private String id;

    private String name;

    private LocationType type;

    private Double latitude;

    private Double longitude;

    private List<LocationChildResponse> children;
}
