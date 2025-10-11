package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventCreationController {

    private final EventCreationService eventCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createEventWithPerson(@PathVariable String treeId, @PathVariable String personId, @RequestBody ParticipantEventRequest participantEventRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        eventCreationService.createEventWithParticipant(CreateEventRequestWithParticipantParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .participantId(personId)
                .eventRequest(participantEventRequest)
                .participantEventRequest(participantEventRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
