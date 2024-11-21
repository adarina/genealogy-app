package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.service.FamilyChildManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenCreationController {

    private final FamilyChildManagementService familyChildManagementService;

    private final PersonCreationService personCreationService;

    public FamilyChildrenCreationController(FamilyChildManagementService familyChildManagementService, PersonCreationService personCreationService) {
        this.familyChildManagementService = familyChildManagementService;
        this.personCreationService = personCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddChildToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonRequest personRequest) {
        Person person = personCreationService.createPerson(treeId, personRequest);
        familyChildManagementService.addChildToFamily(treeId, familyId, person.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
