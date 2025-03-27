package com.ada.genealogyapp.person.controller;

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
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events/{eventId}")
public class PersonEventManagementController {

    private final EventManagementService eventManagementService;

    //TODO upload nie działa ale dla family dizła więc jak na razie nie wiem, może coś na froncie
    @PutMapping
    public ResponseEntity<?> updateEventWithPerson(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEventWithParticipant(UpdateEventRequestWithParticipantParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(personId)
                .eventId(eventId)
                .eventRequest(participantEventRequest)
                .participantEventRequest(participantEventRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removePersonFromEvent(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.removeParticipantFromEvent(RemoveParticipantFromEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .participantId(personId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addPersonToEvent(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEventWithParticipant(UpdateEventRequestWithParticipantParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(personId)
                .eventId(eventId)
                .participantEventRequest(participantEventRequest)
                .build());
        return ResponseEntity.ok().build();
    }
}
