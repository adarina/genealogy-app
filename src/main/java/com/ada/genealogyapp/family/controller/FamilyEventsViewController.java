package com.ada.genealogyapp.family.controller;


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
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventsViewController {

    private final ParticipantEventsViewService participantEventsViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<ParticipantEventResponse>> getFamilyEvents(@PathVariable String treeId, @PathVariable String familyId, @PageableDefault Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<ParticipantEventResponse> eventResponses = participantEventsViewService.getParticipantEvents(GetParticipantEventsParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .participantId(familyId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ParticipantEventResponse> getFamilyEvent(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        ParticipantEventResponse participantEventResponse = participantEventsViewService.getParticipantEvent(GetParticipantEventParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .participantId(familyId)
                .eventId(eventId)
                .build());
        return ResponseEntity.ok(participantEventResponse);
    }
}
