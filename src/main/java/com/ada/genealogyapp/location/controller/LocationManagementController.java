package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.DeleteLocationParams;
import com.ada.genealogyapp.location.dto.params.UpdateLocationRequestParams;
import com.ada.genealogyapp.location.service.LocationManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations/{locationId}")
public class LocationManagementController {

    private final LocationManagementService locationManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateLocation(@PathVariable String treeId, @PathVariable String locationId, @RequestBody LocationRequest locationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationManagementService.updateLocation(UpdateLocationRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .locationRequest(locationRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteLocation(@PathVariable String treeId, @PathVariable String locationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationManagementService.deleteLocation(DeleteLocationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
