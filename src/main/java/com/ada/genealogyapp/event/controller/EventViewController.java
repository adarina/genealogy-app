package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventsResponse;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.dto.params.GetEventsParams;
import com.ada.genealogyapp.event.service.EventViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events")
public class EventViewController {

    private final EventViewService eventViewService;

    @GetMapping
    public ResponseEntity<Page<EventsResponse>> getEvents(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) throws JsonProcessingException {
        Page<EventsResponse> eventResponses = eventViewService.getEvents(GetEventsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        EventResponse eventResponse = eventViewService.getEvent(GetEventParams.builder()
                        .userId(userId)
                        .treeId(treeId)
                        .eventId(eventId)
                        .build());
        return ResponseEntity.ok(eventResponse);
    }
}
