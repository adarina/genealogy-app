package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonEventResponse;
import com.ada.genealogyapp.person.service.PersonEventsViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventsViewController {

    private final PersonEventsViewService personEventsViewService;

    public PersonEventsViewController(PersonEventsViewService personEventsViewService) {
        this.personEventsViewService = personEventsViewService;
    }

    @GetMapping
    public ResponseEntity<Page<PersonEventResponse>> getPersonalEvents(@PathVariable UUID treeId, @PathVariable UUID personId, @PageableDefault Pageable pageable) {
        Page<PersonEventResponse> eventResponses = personEventsViewService.getPersonalEvents(treeId, personId, pageable);
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<PersonEventResponse> getPersonalEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId) {
        PersonEventResponse eventResponse = personEventsViewService.getPersonalEvent(treeId, personId, eventId);
        return ResponseEntity.ok(eventResponse);
    }
}
