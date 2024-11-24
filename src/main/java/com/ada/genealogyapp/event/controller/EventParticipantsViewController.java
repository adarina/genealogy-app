package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.service.EventParticipantsViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/participants")
public class EventParticipantsViewController {

    private final EventParticipantsViewService eventParticipantsViewService;

    public EventParticipantsViewController(EventParticipantsViewService eventParticipantsViewService) {
        this.eventParticipantsViewService = eventParticipantsViewService;
    }

    @GetMapping
    public ResponseEntity<Page<EventParticipantResponse>> getEventParticipants(@PathVariable UUID treeId, @PathVariable UUID eventId, @PageableDefault Pageable pageable) {
        Page<EventParticipantResponse> participantResponses = eventParticipantsViewService.getEventParticipants(treeId, eventId, pageable);
        return ResponseEntity.ok(participantResponses);
    }
}
