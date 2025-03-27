package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventCreationController {

    private final EventCreationService eventCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddFamilyToEvent(@PathVariable String treeId, @PathVariable String familyId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventCreationService.createEventWithParticipant(CreateEventRequestWithParticipantParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(familyId)
                .participantEventRequest(participantEventRequest)
                .eventRequest(participantEventRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}