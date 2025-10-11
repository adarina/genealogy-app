package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.dto.params.DeleteEventParams;
import com.ada.genealogyapp.event.dto.params.UpdateEventRequestParams;
import com.ada.genealogyapp.event.service.EventManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}")
public class EventManagementController {

    private final EventManagementService eventManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody EventRequest eventRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventManagementService.updateEvent(UpdateEventRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .eventId(eventId)
                .eventRequest(eventRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteEvent(@PathVariable String treeId, @PathVariable String eventId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventManagementService.deleteEvent(DeleteEventParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .eventId(eventId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
