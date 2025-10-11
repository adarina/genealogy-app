package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.RemovePersonFromFamilyParams;
import com.ada.genealogyapp.family.service.FamilyMotherManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/mother/{motherId}")
public class FamilyMotherManagementController {

    private final FamilyMotherManagementService familyMotherManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @DeleteMapping
    public ResponseEntity<?> removePersonFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String motherId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        familyMotherManagementService.removeMotherFromFamily(RemovePersonFromFamilyParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .personId(motherId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<String> addMotherToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String motherId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        familyMotherManagementService.addMotherToFamily(AddPersonToFamilyParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .personId(motherId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
