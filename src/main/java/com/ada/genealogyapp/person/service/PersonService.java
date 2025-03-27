package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.params.*;


public interface PersonService {

    void savePerson(SavePersonParams params);

    void deletePerson(DeletePersonParams params);

    void addParentChildRelationship(AddParentChildRelationshipParams params);

    void updatePerson(UpdatePersonParams params);

}
