package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.dto.params.DeleteEventParams;
import com.ada.genealogyapp.event.dto.params.UpdateEventRequestParams;
import com.ada.genealogyapp.event.service.EventManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}")
public class EventManagementController {


    private final EventManagementService eventManagementService;

    @PutMapping
    public ResponseEntity<?> updateEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody EventRequest eventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEvent(UpdateEventRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .eventRequest(eventRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.deleteEvent(DeleteEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
