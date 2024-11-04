package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.person.dto.PersonEventsResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.service.PersonEventsViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventsViewController {

    private final PersonEventsViewService personEventsViewService;

    public PersonEventsViewController(PersonEventsViewService personEventsViewService) {
        this.personEventsViewService = personEventsViewService;
    }

    @GetMapping
    public ResponseEntity<Page<PersonEventsResponse>> getPersonalEvents(@PathVariable UUID treeId, @PathVariable UUID personId, @PageableDefault Pageable pageable) {
        Page<PersonEventsResponse> eventResponses = personEventsViewService.getPersonalEvents(treeId, personId, pageable);
        return ResponseEntity.ok(eventResponses);
    }

}
