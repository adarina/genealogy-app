package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.params.AddSourceToCitationParams;
import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
import com.ada.genealogyapp.event.dto.params.RemoveLocationFromEventParams;
import com.ada.genealogyapp.event.service.EventLocationManagementService;
import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.UpdateLocationRequestWithParentParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/location/{locationId}")
public class EventLocationManagementController {

    private final EventLocationManagementService eventLocationManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @DeleteMapping()
    public ResponseEntity<?> removeLocationFromEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String locationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventLocationManagementService.removeLocationFromEvent(RemoveLocationFromEventParams.builder()
                .treeId(treeId)
                .userId(authentication.getName())
                .eventId(eventId)
                .locationId(locationId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addLocationToEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String locationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventLocationManagementService.addLocationToEvent(AddLocationToEventParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .eventId(eventId)
                .locationId(locationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
