package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.family.service.FamilyCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families")
public class FamilyCreationController {

    private final FamilyCreationService familyCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createFamily(@PathVariable String treeId, @RequestBody FamilyRequest familyRequest) throws ValidationException {
        Authentication authentication = authenticationFacade.getAuthentication();
        familyCreationService.createFamily(CreateFamilyRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyRequest(familyRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
