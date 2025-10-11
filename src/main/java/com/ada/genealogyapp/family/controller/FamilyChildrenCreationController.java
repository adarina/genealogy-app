package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.dto.params.CreateAndAddChildToFamilyParams;
import com.ada.genealogyapp.person.service.PersonCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenCreationController {

    private final PersonCreationService personCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createAndAddChildToFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody FamilyChildRequest familyChildRequest) throws ValidationException {
        Authentication authentication = authenticationFacade.getAuthentication();
        personCreationService.createAndAddChildToFamily(CreateAndAddChildToFamilyParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .familyChildRequest(familyChildRequest)
                .personRequest(familyChildRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
