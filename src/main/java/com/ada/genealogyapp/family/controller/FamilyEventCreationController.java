package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventCreationController {

    private final EventCreationService eventCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createAndAddFamilyToEvent(@PathVariable String treeId, @PathVariable String familyId, @RequestBody ParticipantEventRequest participantEventRequest) throws ValidationException {
        Authentication authentication = authenticationFacade.getAuthentication();
        log.info("relation{}", participantEventRequest.getRelationship());
        eventCreationService.createEventWithParticipant(CreateEventRequestWithParticipantParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .participantId(familyId)
                .participantEventRequest(participantEventRequest)
                .eventRequest(participantEventRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
