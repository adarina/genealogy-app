package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.service.FamilyPersonsManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/persons")
public class FamilyPersonsManagementController {

    private final FamilyPersonsManagementService familyManagementService;
    private final PersonCreationService personCreationService;

    public FamilyPersonsManagementController(FamilyPersonsManagementService familyManagementService, PersonCreationService personCreationService) {
        this.familyManagementService = familyManagementService;
        this.personCreationService = personCreationService;
    }

    @PostMapping("/addExistingFather")
    public ResponseEntity<?> addFatherToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody UUIDRequest UUIDRequest) {
        familyManagementService.addFatherToFamily(treeId, familyId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addExistingMother")
    public ResponseEntity<?> addMotherToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody UUIDRequest UUIDRequest) {
        familyManagementService.addMotherToFamily(treeId, familyId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @PostMapping("/addNewFather")
    public ResponseEntity<?> createAndAddFatherToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonRequest personRequest) {
        Person father = personCreationService.createPerson(treeId, personRequest);
        familyManagementService.addFatherToFamily(treeId, familyId, father.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewMother")
    public ResponseEntity<?> createAndAddMotherToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonRequest personRequest) {
        Person mother = personCreationService.createPerson(treeId, personRequest);
        familyManagementService.addMotherToFamily(treeId, familyId, mother.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removePersonFromFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody UUIDRequest UUIDRequest) {
        familyManagementService.removePersonFromFamily(treeId, familyId, UUIDRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
