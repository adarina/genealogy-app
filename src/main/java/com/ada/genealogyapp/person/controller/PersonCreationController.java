package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.person.dto.PersonCreatedResponse;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.CreatePersonRequestParams;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons")
public class PersonCreationController {

    private final PersonCreationService personCreationService;

    private final IAuthenticationFacade authenticationFacade;

    private final Clock clock;

    @PostMapping
    public ResponseEntity<PersonCreatedResponse> createPerson(@PathVariable String treeId, @RequestBody PersonRequest personRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Person person = personCreationService.createPerson(CreatePersonRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .personRequest(personRequest)
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(PersonCreatedResponse.builder()
                .firstname(person.getFirstname())
                .lastname(person.getLastname())
                .gender(person.getGender())
                .createTime(LocalDateTime.now(clock))
                .build());
    }
}
