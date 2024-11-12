package com.ada.genealogyapp.person.controller;


import com.ada.genealogyapp.person.dto.PersonFamiliesResponse;
import com.ada.genealogyapp.person.service.PersonFamiliesViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<PersonFamiliesResponse>> getPersonalFamilies(@PathVariable UUID treeId, @PathVariable UUID personId,  @PageableDefault Pageable pageable) {
        Page<PersonFamiliesResponse> personFamiliesResponses = personFamiliesViewService.getPersonalFamilies(treeId, personId, pageable);
        return ResponseEntity.ok(personFamiliesResponses);
    }
}
