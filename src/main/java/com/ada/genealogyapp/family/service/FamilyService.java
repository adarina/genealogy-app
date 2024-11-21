package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.model.Family;

import java.util.UUID;

public interface FamilyService {

    void saveFamily(Family family);
    Family findFamilyById(UUID familyId);
    void ensureFamilyExists(UUID familyId);
}
