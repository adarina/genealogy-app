package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.service.PersonManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}")
public class PersonManagementController {

    private final PersonManagementService personManagementService;

    public PersonManagementController(PersonManagementService personManagementService) {
        this.personManagementService = personManagementService;
    }


    @PutMapping
    public ResponseEntity<?> updatePerson(@PathVariable String treeId, @PathVariable String personId, @RequestBody PersonRequest personRequest) {
        personManagementService.updatePerson(treeId, personId, personRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deletePerson(@PathVariable String treeId, @PathVariable String personId) {
        personManagementService.deletePerson(treeId, personId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
