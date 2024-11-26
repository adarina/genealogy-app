package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;


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

    @TransactionalInNeo4j
    public void deleteFamily(Family family) {
        familyRepository.delete(family);
        log.info("Family deleted successfully: {}", family.getId());
    }


    @TransactionalInNeo4j
    public void updateFamiliesNames(List<Family> families, Person person) {
        for (Family family : families) {
            if (nonNull(family.getFather()) && family.getFather().getId().equals(person.getId())) {
                family.setName(person.getName() + " & " +
                        (nonNull(family.getMother()) ? family.getMother().getName() : ""));
            }
            if (nonNull(family.getMother()) && family.getMother().getId().equals(person.getId())) {
                family.setName((nonNull(family.getFather()) ? family.getFather().getName() : "") + " & " + person.getName());
            }
            familyRepository.save(family);
        }
    }

    @TransactionalInNeo4j
    public List<Family> findFamiliesByPerson(Person person) {
        return familyRepository.findByFatherIdOrMotherId(person.getId(), person.getId());
    }
}
