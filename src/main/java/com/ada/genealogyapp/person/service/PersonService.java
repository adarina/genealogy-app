package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;

import java.util.UUID;

public interface PersonService {

    Person findPersonById(UUID personId);

    void savePerson(Person person);

    void ensurePersonExists(UUID personId);

    void deletePerson(Person person);

    PersonResponse findPersonResponseById(UUID personId);

}
