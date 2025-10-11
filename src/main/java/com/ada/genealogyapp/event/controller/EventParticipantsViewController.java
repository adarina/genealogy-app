package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.dto.params.GetEventParticipantsParams;
import com.ada.genealogyapp.event.service.EventParticipantsViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/participants")
public class EventParticipantsViewController {

    private final EventParticipantsViewService eventParticipantsViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<EventParticipantResponse>> getEventParticipants(@PathVariable String treeId, @PathVariable String eventId, @PageableDefault Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<EventParticipantResponse> participantResponses = eventParticipantsViewService.getEventParticipants(GetEventParticipantsParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(participantResponses);
    }
}
