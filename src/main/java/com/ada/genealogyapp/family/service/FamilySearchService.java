package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FamilySearchService {

    private final FamilyRepository familyRepository;

    public FamilySearchService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public Family findFamilyById(UUID familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new NodeNotFoundException("No family found with ID: {}" + familyId));
    }

    public List<Family> getFamiliesByTreeId(UUID treeId) {
        return familyRepository.findAllByFamilyTree_Id(treeId);
    }
}

