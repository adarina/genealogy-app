package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventCreationController {

    private final EventCreationService eventCreationService;

    @PostMapping
    public ResponseEntity<?> createEventWithPerson(@PathVariable String treeId, @PathVariable String personId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventCreationService.createEventWithParticipant(CreateEventRequestWithParticipantParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(personId)
                .eventRequest(participantEventRequest)
                .participantEventRequest(participantEventRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}