package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;

import java.util.UUID;

public interface PersonService {

    Person findPersonByIdOrThrowNodeNotFoundException(UUID personId);

    Person findPersonById(UUID childId);

    void savePerson(Person person);

    void ensurePersonExists(UUID personId);

    void deletePerson(Person person);

}
