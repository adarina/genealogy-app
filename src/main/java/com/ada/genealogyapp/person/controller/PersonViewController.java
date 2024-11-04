package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.service.PersonViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons")
public class PersonViewController {

    private final PersonViewService personViewService;

    public PersonViewController(PersonViewService personViewService) {
        this.personViewService = personViewService;
    }

    @GetMapping
    public ResponseEntity<Page<PersonResponse>> getPersons(@PathVariable UUID treeId, @PageableDefault Pageable pageable) {
        Page<PersonResponse> personResponses = personViewService.getPersons(treeId, pageable);
        return ResponseEntity.ok(personResponses);
    }


}
