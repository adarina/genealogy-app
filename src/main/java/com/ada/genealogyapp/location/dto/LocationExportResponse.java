package com.ada.genealogyapp.location.dto;

import com.ada.genealogyapp.location.type.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationExportResponse {

    private String id;

    private String name;

    private Boolean isMain;

    private LocationType type;

    private Double latitude;

    private Double longitude;

    private String locationId;
}
