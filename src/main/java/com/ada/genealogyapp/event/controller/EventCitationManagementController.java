package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.params.AddCitationToEventParams;
import com.ada.genealogyapp.event.dto.params.RemoveCitationFromEventParams;
import com.ada.genealogyapp.event.service.EventCitationManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations/{citationId}")
public class EventCitationManagementController {

    private final EventCitationManagementService eventCitationManagementService;

    @DeleteMapping()
    public ResponseEntity<?> removeCitationFromEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String citationId, @RequestHeader(value = "X-User-Id") String userId) {
        eventCitationManagementService.removeCitationFromEvent(RemoveCitationFromEventParams.builder()
                .treeId(treeId)
                .userId(userId)
                .eventId(eventId)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addCitationToEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String citationId, @RequestHeader(value = "X-User-Id") String userId) {
        eventCitationManagementService.addCitationToEvent(AddCitationToEventParams.builder()
                .treeId(treeId)
                .userId(userId)
                .eventId(eventId)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
