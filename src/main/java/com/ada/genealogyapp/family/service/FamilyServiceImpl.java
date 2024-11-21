package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class FamilyServiceImpl implements FamilyService{

    private final FamilyRepository familyRepository;

    public FamilyServiceImpl(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    @TransactionalInNeo4j
    public void saveFamily(Family family) {
        Family savedFamily = familyRepository.save(family);
        log.info("Family saved successfully: {}", savedFamily);
    }

    public Family findFamilyById(UUID familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new NodeNotFoundException("Family not found with ID: " + familyId));
    }

    public void ensureFamilyExists(UUID familyId) {
        if (!familyRepository.existsById(familyId)) {
            throw new NodeNotFoundException("Family not found with ID: " + familyId);
        }
    }
}
