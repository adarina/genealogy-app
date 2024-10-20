package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.service.PersonManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}")
public class PersonManagementController {

    private final PersonManagementService personManagementService;

    public PersonManagementController(PersonManagementService personManagementService) {
        this.personManagementService = personManagementService;
    }

    @PostMapping()
    public ResponseEntity<Void> startEditingPerson(@PathVariable UUID treeId, @PathVariable UUID personId) {
        personManagementService.startTransactionAndSession(treeId, personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updatePersonalData")
    public ResponseEntity<?> updatePerson(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody PersonRequest personRequest) {
        personManagementService.updatePersonalData(treeId, personId, personRequest);
        return ResponseEntity.ok().build();
    }
}
