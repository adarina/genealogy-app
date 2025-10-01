package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.citation.dto.params.AddSourceToCitationParams;
import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
import com.ada.genealogyapp.event.dto.params.RemoveLocationFromEventParams;
import com.ada.genealogyapp.event.service.EventLocationManagementService;
import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.UpdateLocationRequestWithParentParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/location/{locationId}")
public class EventLocationManagementController {

    private final EventLocationManagementService eventLocationManagementService;

    @DeleteMapping()
    public ResponseEntity<?> removeLocationFromEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String locationId, @RequestHeader(value = "X-User-Id") String userId) {
        eventLocationManagementService.removeLocationFromEvent(RemoveLocationFromEventParams.builder()
                .treeId(treeId)
                .userId(userId)
                .eventId(eventId)
                .locationId(locationId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addLocationToEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String locationId, @RequestHeader(value = "X-User-Id") String userId) {
        eventLocationManagementService.addLocationToEvent(AddLocationToEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .locationId(locationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
