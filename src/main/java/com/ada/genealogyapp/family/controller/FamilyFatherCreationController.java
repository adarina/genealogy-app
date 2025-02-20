package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyFatherManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/father")
public class FamilyFatherCreationController {

    private final PersonCreationService personCreationService;

    private final FamilyFatherManagementService familyFatherManagementService;

    public FamilyFatherCreationController(PersonCreationService personCreationService, FamilyFatherManagementService familyFatherManagementService) {
        this.personCreationService = personCreationService;
        this.familyFatherManagementService = familyFatherManagementService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddFatherToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody PersonRequest personRequest, @RequestHeader(value = "X-User-Id") String userId) {
        Person father = personCreationService.createPerson(userId, treeId, personRequest);
        familyFatherManagementService.addFatherToFamily(userId, treeId, familyId, father.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
