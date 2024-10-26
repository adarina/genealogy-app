package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonEventsResponse;
import com.ada.genealogyapp.person.service.PersonEventsViewService;
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
    public ResponseEntity<PersonEventsResponse> getPersonalEvents(@PathVariable UUID treeId, @PathVariable UUID personId) {
        PersonEventsResponse personEventInfo = personEventsViewService.getPersonalEvents(treeId, personId);
        return ResponseEntity.ok(personEventInfo);
    }
}
