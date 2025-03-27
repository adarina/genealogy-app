package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.dto.params.CreateAndAddChildToFamilyParams;
import com.ada.genealogyapp.person.dto.params.CreateAndAddPersonToFamilyParams;
import com.ada.genealogyapp.person.service.PersonCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenCreationController {

    private final PersonCreationService personCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddChildToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody FamilyChildRequest familyChildRequest, @RequestHeader(value = "X-User-Id") String userId) {
        personCreationService.createAndAddChildToFamily(CreateAndAddChildToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .familyChildRequest(familyChildRequest)
                .personRequest(familyChildRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
