package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.CreateAndAddPersonToFamilyParams;
import com.ada.genealogyapp.person.service.PersonCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/father")
public class FamilyFatherCreationController {

    private final PersonCreationService personCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createAndAddFatherToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody PersonRequest personRequest) throws ValidationException {
        Authentication authentication = authenticationFacade.getAuthentication();
        personCreationService.createAndAddFatherToFamily(CreateAndAddPersonToFamilyParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .personRequest(personRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
