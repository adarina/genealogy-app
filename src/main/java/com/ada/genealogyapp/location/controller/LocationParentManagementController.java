package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.RemoveParentFromLocationParams;
import com.ada.genealogyapp.location.dto.params.UpdateLocationRequestWithParentParams;
import com.ada.genealogyapp.location.service.LocationManagementService;
import com.ada.genealogyapp.location.service.LocationParentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations/{locationId}/parent/{parentId}")
public class LocationParentManagementController {

    private final LocationParentManagementService locationParentManagementService;

    private final LocationManagementService locationManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @DeleteMapping
    public ResponseEntity<?> removeParentFromLocation(@PathVariable String treeId, @PathVariable String locationId, @PathVariable String parentId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationParentManagementService.removeParentFromLocation(RemoveParentFromLocationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .parentId(parentId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<?> updateLocationWithParent(@PathVariable String treeId, @PathVariable String locationId, @PathVariable String parentId, @RequestBody LocationRequest locationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationManagementService.updateLocationWithParent(UpdateLocationRequestWithParentParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .parentId(parentId)
                .locationId(locationId)
                .locationRequest(locationRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addParentToLocation(@PathVariable String treeId, @PathVariable String locationId, @PathVariable String parentId, @RequestBody LocationRequest locationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationManagementService.updateLocationWithParent(UpdateLocationRequestWithParentParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .parentId(parentId)
                .locationRequest(locationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
