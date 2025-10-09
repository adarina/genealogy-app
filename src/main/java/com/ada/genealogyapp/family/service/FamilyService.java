package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.params.*;
import com.ada.genealogyapp.person.dto.params.UpdateChildInFamilyParams;

import java.util.List;
import java.util.Map;

public interface FamilyService {

    void saveFamily(SaveFamilyParams params);

    void updateFamily(UpdateFamilyParams params);

    void deleteFamily(DeleteFamilyParams params);

    void addFatherToFamily(AddPersonToFamilyParams params);

    void addMotherToFamily(AddPersonToFamilyParams params);

    void addChildToFamily(AddChildToFamilyRequestParams params);

    void removeFatherFromFamily(RemovePersonFromFamilyParams params);

    void removeMotherFromFamily(RemovePersonFromFamilyParams params);

    void removeChildFromFamily(RemovePersonFromFamilyParams params);

    void updateChildInFamily(UpdateChildInFamilyParams params);

    void saveFamilies(String userId, String treeId, List<Map<String, Object>> familiesData);

    void addFamilyRelationships(String userId, String treeId, List<Map<String, Object>> fathersData, List<Map<String, Object>> mothersData, List<Map<String, Object>> childrenData);
}
