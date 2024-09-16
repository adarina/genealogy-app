package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/persons")
public class PersonCreationController {

    private final PersonCreationService personCreationService;

    public PersonCreationController(PersonCreationService personCreationService) {
        this.personCreationService = personCreationService;

    }

    @PostMapping
    public ResponseEntity<?> createPerson(@PathVariable UUID treeId, @RequestBody PersonRequest personRequest) {
        personCreationService.createPerson(treeId, personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
