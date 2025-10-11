package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.CreateLocationRequestParams;
import com.ada.genealogyapp.location.service.LocationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations")
public class LocationCreationController {

    private final LocationCreationService locationCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createLocation(@PathVariable String treeId, @RequestBody LocationRequest locationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationCreationService.createLocation(CreateLocationRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationRequest(locationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
