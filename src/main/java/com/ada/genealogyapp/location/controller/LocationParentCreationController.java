package com.ada.genealogyapp.location.controller;


import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.CreateAndAddParentToLocationRequestParams;
import com.ada.genealogyapp.location.service.LocationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations/{locationId}/parent")
public class LocationParentCreationController {

    private final LocationCreationService locationCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddParentToLocation(@PathVariable String treeId, @PathVariable String locationId, @RequestBody LocationRequest locationRequest, @RequestHeader(value = "X-User-Id") String userId) {
        locationCreationService.createAndAddParentToLocation(CreateAndAddParentToLocationRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .locationRequest(locationRequest)
                .locationId(locationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
