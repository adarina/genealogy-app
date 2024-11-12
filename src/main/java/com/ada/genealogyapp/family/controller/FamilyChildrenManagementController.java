package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.service.FamilyChildrenManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenManagementController {

    private final FamilyChildrenManagementService familyChildrenManagementService;

    private final PersonCreationService personCreationService;

    public FamilyChildrenManagementController(FamilyChildrenManagementService familyChildrenManagementService, PersonCreationService personCreationService) {
        this.familyChildrenManagementService = familyChildrenManagementService;
        this.personCreationService = personCreationService;
    }


    @PostMapping("/addExistingChild")
    public ResponseEntity<?> addChildToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody UUIDRequest UUIDRequest) {
        familyChildrenManagementService.addExistingChildToFamily(treeId, familyId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewChild")
    public ResponseEntity<?> createAndAddChildToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonRequest personRequest) {
        Person person = personCreationService.createPerson(treeId, personRequest);
        familyChildrenManagementService.addExistingChildToFamily(treeId, familyId, person.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
