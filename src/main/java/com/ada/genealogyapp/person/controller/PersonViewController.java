package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.service.PersonViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons")
public class PersonViewController {

    private final PersonViewService personViewService;

    public PersonViewController(PersonViewService personViewService) {
        this.personViewService = personViewService;
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonResponse> getPerson(@PathVariable String treeId, @PathVariable String personId) {
        PersonResponse personResponse = personViewService.getPerson(treeId, personId);
        return ResponseEntity.ok(personResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PersonResponse>> getPersons(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) throws JsonProcessingException {
        Page<PersonResponse> personResponses = personViewService.getPersons(userId, treeId, filter, pageable);
        return ResponseEntity.ok(personResponses);
    }
}
