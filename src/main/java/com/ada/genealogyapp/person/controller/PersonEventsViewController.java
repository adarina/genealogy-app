package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.service.ParticipantEventsViewService;
import com.ada.genealogyapp.person.dto.params.GetParticipantEventParams;
import com.ada.genealogyapp.person.dto.params.GetParticipantEventsParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventsViewController {

    private final ParticipantEventsViewService participantEventsViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<ParticipantEventResponse>> getPersonEvents(@PathVariable String treeId, @PathVariable String personId, @PageableDefault Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<ParticipantEventResponse> eventResponses = participantEventsViewService.getParticipantEvents(GetParticipantEventsParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .participantId(personId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ParticipantEventResponse> getPersonEvent(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        ParticipantEventResponse participantEventResponse = participantEventsViewService.getParticipantEvent(GetParticipantEventParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .participantId(personId)
                .eventId(eventId)
                .build());
        return ResponseEntity.ok(participantEventResponse);
    }
}
