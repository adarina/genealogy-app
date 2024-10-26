package com.ada.genealogyapp.person.controller;


import com.ada.genealogyapp.person.dto.PersonFamiliesResponse;
import com.ada.genealogyapp.person.service.PersonFamiliesViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/families")
public class PersonFamiliesViewController {

    private final PersonFamiliesViewService personFamiliesViewService;

    public PersonFamiliesViewController(PersonFamiliesViewService personFamiliesViewService) {
        this.personFamiliesViewService = personFamiliesViewService;
    }

    @GetMapping
    public ResponseEntity<PersonFamiliesResponse> getPersonalFamilies(@PathVariable UUID treeId, @PathVariable UUID personId) {
        PersonFamiliesResponse personFamilyInfo = personFamiliesViewService.getPersonalFamilies(treeId, personId);
        return ResponseEntity.ok(personFamilyInfo);
    }
}
