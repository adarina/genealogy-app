package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.LocationChildResponse;
import com.ada.genealogyapp.location.dto.params.GetLocationParams;
import com.ada.genealogyapp.location.dto.LocationResponse;
import com.ada.genealogyapp.location.service.LocationViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations")
public class LocationViewController {

    private final LocationViewService locationViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable String treeId, @PathVariable String locationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LocationResponse locationResponse = locationViewService.getLocation(GetLocationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .build());
        return ResponseEntity.ok(locationResponse);
    }

    @GetMapping
    public ResponseEntity<List<LocationChildResponse>> getLocationsParents(@PathVariable String treeId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        List<LocationChildResponse> locationsResponse = locationViewService.getLocationsParents(GetLocationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .build());
        return ResponseEntity.ok(locationsResponse);
    }
}
