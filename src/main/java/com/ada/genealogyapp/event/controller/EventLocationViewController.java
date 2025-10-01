package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.service.EventLocationViewService;
import com.ada.genealogyapp.location.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/location")
public class EventLocationViewController {

    private final EventLocationViewService eventLocationViewService;

    @GetMapping
    public ResponseEntity<LinkedHashSet<LocationResponse>> getEventLocation(@PathVariable String treeId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        LinkedHashSet<LocationResponse> locationResponses = eventLocationViewService.getEventLocation(GetEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build());
        return ResponseEntity.ok(locationResponses);
    }
}
