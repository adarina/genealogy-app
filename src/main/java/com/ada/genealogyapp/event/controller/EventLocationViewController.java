package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.service.EventLocationViewService;
import com.ada.genealogyapp.location.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/location")
public class EventLocationViewController {

    private final EventLocationViewService eventLocationViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<LinkedHashSet<LocationResponse>> getEventLocation(@PathVariable String treeId, @PathVariable String eventId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LinkedHashSet<LocationResponse> locationResponses = eventLocationViewService.getEventLocation(GetEventParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .eventId(eventId)
                .build());
        return ResponseEntity.ok(locationResponses);
    }
}
