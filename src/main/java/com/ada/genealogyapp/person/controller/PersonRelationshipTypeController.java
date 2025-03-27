package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.type.PersonRelationshipType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/personRelationship")
public class PersonRelationshipTypeController {

    @GetMapping
    public List<PersonRelationshipType> getPersonRelationshipTypes() {
        return Arrays.asList(PersonRelationshipType.values());
    }
}
