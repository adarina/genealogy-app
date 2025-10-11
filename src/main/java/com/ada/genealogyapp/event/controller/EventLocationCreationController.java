package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.location.dto.params.CreateAndAddLocationToEventRequestParams;
import com.ada.genealogyapp.location.service.LocationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/location")
public class EventLocationCreationController {

    private final LocationCreationService locationCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createAndAddLocationToEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody LocationRequest locationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        locationCreationService.createAndAddLocationToEvent(CreateAndAddLocationToEventRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationRequest(locationRequest)
                .eventId(eventId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
