package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.CreateLocationRequestParams;
import com.ada.genealogyapp.location.service.LocationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations")
public class LocationCreationController {

    private final LocationCreationService locationCreationService;

    @PostMapping
    public ResponseEntity<?> createLocation(@PathVariable String treeId, @RequestBody LocationRequest locationRequest, @RequestHeader(value = "X-User-Id") String userId) {
        locationCreationService.createLocation(CreateLocationRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .locationRequest(locationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
