package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.model.Family;

public interface FamilyService {

    void saveFamily(Family family);

    void updateFamily(String treeId, String familyId, Family family);

    Family findFamilyById(String familyId);

    void ensureFamilyExists(String familyId);

    void deleteFamily(String treeId, String familyId);

    Family findFamilyByTreeIdAndId(String treeId, String familyId);

    void addFatherToFamily(String treeId, String familyId, String fatherId);

    void addMotherToFamily(String treeId, String familyId, String motherId);

    void addChildToFamily(String treeId, String familyId, String childId, String fatherRelationshipType, String motherRelationshipType);

    void removeFatherFromFamily(String treeId, String familyId, String fatherId);

    void removeMotherFromFamily(String treeId, String familyId, String motherId);

    void removeChildFromFamily(String treeId, String familyId, String childId);

    void updateChildInFamily(String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest);
}
