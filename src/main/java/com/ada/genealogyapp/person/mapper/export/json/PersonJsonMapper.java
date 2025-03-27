package com.ada.genealogyapp.person.mapper.export.json;

import com.ada.genealogyapp.export.json.JsonMapper;
import com.ada.genealogyapp.person.dto.PersonRelationshipRequest;
import com.ada.genealogyapp.person.dto.PersonJsonRequest;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PersonJsonMapper implements JsonMapper<Person, PersonJsonRequest> {

    @Override
    public PersonJsonRequest map(Person person) {
        return PersonJsonRequest.builder()
                .id(person.getId())
                .name(person.getName())
                .firstname(person.getFirstname())
                .lastname(person.getLastname())
                .gender(person.getGender())
                .relationships(person.getRelationships().stream()
                        .map(relationship -> PersonRelationshipRequest.builder()
                                .id(relationship.getId())
                                .childId(relationship.getChild().getId())
                                .relationship(relationship.getRelationship())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Class<Person> getEntityType() {
        return Person.class;
    }
}

