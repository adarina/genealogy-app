package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyMotherManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/mother")
public class FamilyMotherCreationController {

    private final PersonCreationService personCreationService;

    private final FamilyMotherManagementService familyMotherManagementService;

    public FamilyMotherCreationController(PersonCreationService personCreationService, FamilyMotherManagementService familyMotherManagementService) {
        this.personCreationService = personCreationService;
        this.familyMotherManagementService = familyMotherManagementService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddMotherToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody PersonRequest personRequest) {
        Person mother = personCreationService.createPerson(treeId, personRequest);
        familyMotherManagementService.addMotherToFamily(treeId, familyId, mother.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
