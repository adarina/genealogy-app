package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.dto.params.CreateEventRequestParams;
import com.ada.genealogyapp.event.service.EventCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events")
public class EventCreationController {

    private final EventCreationService eventCreationService;

    @PostMapping
    public ResponseEntity<?> createEvent(@PathVariable String treeId, @RequestBody EventRequest eventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventCreationService.createEvent(CreateEventRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventRequest(eventRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
