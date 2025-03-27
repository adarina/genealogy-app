package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.params.GetEventCitationsParams;
import com.ada.genealogyapp.event.service.EventCitationsViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations")
public class EventCitationsViewController {

    private final EventCitationsViewService eventCitationsViewService;

    @GetMapping
    public ResponseEntity<Page<EventCitationResponse>> getEventCitations(@PathVariable String treeId, @PathVariable String eventId, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) {
        Page<EventCitationResponse> citationResponses = eventCitationsViewService.getEventCitations(GetEventCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(citationResponses);
    }
}