package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.CreatePersonRequestParams;
import com.ada.genealogyapp.person.service.PersonCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons")
public class PersonCreationController {

    private final PersonCreationService personCreationService;

    @PostMapping
    public ResponseEntity<?> createPerson(@PathVariable String treeId, @RequestBody PersonRequest personRequest, @RequestHeader(value = "X-User-Id") String userId) {
        personCreationService.createPerson(CreatePersonRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .personRequest(personRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
