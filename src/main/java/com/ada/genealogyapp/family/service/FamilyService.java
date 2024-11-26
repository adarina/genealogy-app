package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;

import java.util.List;
import java.util.UUID;

public interface FamilyService {

    void saveFamily(Family family);

    Family findFamilyById(UUID familyId);

    void ensureFamilyExists(UUID familyId);

    void deleteFamily(Family family);

    void updateFamiliesNames(List<Family> families, Person person);

    List<Family> findFamiliesByPerson(Person person);
}
