package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.dto.params.DeleteFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyRequestParams;
import com.ada.genealogyapp.family.service.FamilyManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}")
public class FamilyManagementController {

    private final FamilyManagementService familyManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody FamilyRequest familyRequest) throws ValidationException {
        Authentication authentication = authenticationFacade.getAuthentication();
        familyManagementService.updateFamily(UpdateFamilyRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .familyRequest(familyRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFamily(@PathVariable String treeId, @PathVariable String familyId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        familyManagementService.deleteFamily(DeleteFamilyParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
