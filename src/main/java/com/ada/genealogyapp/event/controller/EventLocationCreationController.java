package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.CreateAndAddLocationToEventRequestParams;
import com.ada.genealogyapp.location.service.LocationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/location")
public class EventLocationCreationController {

    private final LocationCreationService locationCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddLocationToEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody LocationRequest locationRequest, @RequestHeader(value = "X-User-Id") String userId) {
        locationCreationService.createAndAddLocationToEvent(CreateAndAddLocationToEventRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .locationRequest(locationRequest)
                .eventId(eventId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
