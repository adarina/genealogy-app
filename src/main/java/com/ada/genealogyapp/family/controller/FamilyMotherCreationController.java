package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.CreateAndAddPersonToFamilyParams;
import com.ada.genealogyapp.person.service.PersonCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/mother")
public class FamilyMotherCreationController {

    private final PersonCreationService personCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddMotherToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody PersonRequest personRequest, @RequestHeader(value = "X-User-Id") String userId) {
        personCreationService.createAndAddMotherToFamily(CreateAndAddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personRequest(personRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}