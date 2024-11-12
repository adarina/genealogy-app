package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FamilySearchService {

    private final FamilyRepository familyRepository;

    public FamilySearchService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public Family findFamilyByIdOrThrowNodeNotFoundException(UUID familyId) {
        Optional<Family> family = familyRepository.findById(familyId);
        if (family.isPresent()) {
            log.info("Family found: {}", family.get());
        } else {
            log.error("No family found with id: {}", familyId);
            throw new NodeNotFoundException("No family found with id: " + familyId);
        }
        return family.get();
    }

    public List<Family> getFamiliesByTreeIdOrThrowNodeNotFoundException(UUID treeId) {
        List<Family> families = familyRepository.findAllByTree_Id(treeId);
        if (!families.isEmpty()) {
            log.info("Families found for treeId {}: {}", treeId, families);
        } else {
            log.error("No families found for treeId: {}", treeId);
            throw new NodeNotFoundException("No families found for treeId: " + treeId);
        }
        return families;
    }
}
