package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;

import java.util.List;

public interface FamilyService {

    void saveFamily(Family family);

    Family findFamilyById(String familyId);

    void ensureFamilyExists(String familyId);

    void deleteFamily(Family family);

    void updateFamiliesNames(List<Family> families, Person person);

    List<Family> findFamiliesByPerson(Person person);
}
