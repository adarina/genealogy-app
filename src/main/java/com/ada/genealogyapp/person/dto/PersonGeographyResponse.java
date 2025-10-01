package com.ada.genealogyapp.person.dto;


import com.ada.genealogyapp.location.dto.GeographyResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonGeographyResponse {

    private String id;

    private List<GeographyResponse> locations;
}
