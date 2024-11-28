package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;

public interface PersonService {

    Person findPersonById(String personId);

    void savePerson(Person person);

    void ensurePersonExists(String personId);

    void deletePerson(Person person);

    PersonResponse findPersonResponseById(String personId);

}
