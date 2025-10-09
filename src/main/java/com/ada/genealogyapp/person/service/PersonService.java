package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.params.*;

import java.util.List;
import java.util.Map;


public interface PersonService {

    void savePerson(SavePersonParams params);

    void deletePerson(DeletePersonParams params);

    void addParentChildRelationship(AddParentChildRelationshipParams params);

    void updatePerson(UpdatePersonParams params);

    void savePersons(String userId, String treeId, List<Map<String, Object>> personsData);

    void addParentChildRelationships(String id, List<Map<String, Object>> relationshipsData);
}
