package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.DeletePersonParams;
import com.ada.genealogyapp.person.dto.params.UpdatePersonRequestParams;
import com.ada.genealogyapp.person.service.PersonManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}")
public class PersonManagementController {

    private final PersonManagementService personManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updatePerson(@PathVariable String treeId, @PathVariable String personId, @RequestBody PersonRequest personRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        personManagementService.updatePerson(UpdatePersonRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .personId(personId)
                .personRequest(personRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deletePerson(@PathVariable String treeId, @PathVariable String personId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        personManagementService.deletePerson(DeletePersonParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .personId(personId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
