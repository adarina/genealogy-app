package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.model.Family;

import java.util.List;
import java.util.UUID;

public interface FamilyService {

    void saveFamily(Family family);

    Family findFamilyByIdOrThrowNodeNotFoundException(UUID familyId);

    List<Family> getFamiliesByTreeId(UUID treeId);
}
