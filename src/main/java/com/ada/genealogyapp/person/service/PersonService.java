package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;

public interface PersonService {

    Person findPersonById(String personId);

    void savePerson(Person person);

    void ensurePersonExists(String personId);

    void deletePerson(String treeId, String personId);

    PersonResponse findPersonResponseByTreeIdAndId(String treeId, String personId);

    void addParentChildRelationship(String treeId, String personId, String childId, String relationshipType);

    void updatePerson(String treeId, String personId, Person person);

    Person findPersonByTreeIdAndPersonId(String treeId, String personId);



}
