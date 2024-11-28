package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.service.FamilyChildManagementService;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createAndAddChildToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody FamilyChildRequest familyChildRequest) {
        Person person = personCreationService.createPerson(treeId, familyChildRequest);
        familyChildManagementService.addChildToFamily(treeId, familyId, person.getId(), familyChildRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}