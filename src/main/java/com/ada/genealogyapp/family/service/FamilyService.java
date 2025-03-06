package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.model.Family;

public interface FamilyService {

    void saveFamily(String userId, String treeId, Family family);

    void updateFamily(String userId, String treeId, String familyId, Family family);

    void ensureFamilyExists(String familyId);

    void deleteFamily(String userId, String treeId, String familyId);

    void addFatherToFamily(String userId, String treeId, String familyId, String fatherId);

    void addMotherToFamily(String userId, String treeId, String familyId, String motherId);

    void addChildToFamily(String userId, String treeId, String familyId, String childId, String fatherRelationshipType, String motherRelationshipType);

    void removeFatherFromFamily(String userId, String treeId, String familyId, String fatherId);

    void removeMotherFromFamily(String userId, String treeId, String familyId, String motherId);

    void removeChildFromFamily(String userId, String treeId, String familyId, String childId);

    void updateChildInFamily(String userId, String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest);
}
