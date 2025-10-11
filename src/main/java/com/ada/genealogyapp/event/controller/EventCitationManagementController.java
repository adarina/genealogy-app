package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.event.dto.params.AddCitationToEventParams;
import com.ada.genealogyapp.event.dto.params.RemoveCitationFromEventParams;
import com.ada.genealogyapp.event.service.EventCitationManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations/{citationId}")
public class EventCitationManagementController {

    private final EventCitationManagementService eventCitationManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @DeleteMapping()
    public ResponseEntity<?> removeCitationFromEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String citationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventCitationManagementService.removeCitationFromEvent(RemoveCitationFromEventParams.builder()
                .treeId(treeId)
                .userId(authentication.getName())
                .eventId(eventId)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addCitationToEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String citationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventCitationManagementService.addCitationToEvent(AddCitationToEventParams.builder()
                .treeId(treeId)
                .userId(authentication.getName())
                .eventId(eventId)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
