package com.ada.genealogyapp.location.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LocationWithParentsResponse extends LocationResponse {

    private List<LocationResponse> parentLocations;

}
