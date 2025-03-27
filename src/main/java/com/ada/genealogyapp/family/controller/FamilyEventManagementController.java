package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.dto.params.RemoveParticipantFromEventParams;
import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.event.dto.params.UpdateEventRequestWithParticipantParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}")
public class FamilyEventManagementController {

    private final EventManagementService eventManagementService;

    @PutMapping
    public ResponseEntity<?> updateEventInFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEventWithParticipant(UpdateEventRequestWithParticipantParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(familyId)
                .eventId(eventId)
                .participantEventRequest(participantEventRequest)
                .eventRequest(participantEventRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addFamilyToEvent(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEventWithParticipant(UpdateEventRequestWithParticipantParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(familyId)
                .eventId(eventId)
                .participantEventRequest(participantEventRequest)
                .eventRequest(participantEventRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeFamilyFromEvent(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.removeParticipantFromEvent(RemoveParticipantFromEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .participantId(familyId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

