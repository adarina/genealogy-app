package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyFindResponse;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
public class FamilyServiceImpl implements FamilyService {


    private final FamilyRepository familyRepository;

    private final QueryResultProcessor queryResultProcessor;

    public FamilyServiceImpl(FamilyRepository familyRepository, QueryResultProcessor queryResultProcessor) {
        this.familyRepository = familyRepository;
        this.queryResultProcessor = queryResultProcessor;
    }


    public Family findFamilyById(String familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new NodeNotFoundException("Family not found with ID: " + familyId));
    }

//    public Family findFamilyByTreeIdAndId(String userId, String treeId, String familyId) {
//        FamilyFindResponse familyFindResponse = familyRepository.findByTreeIdAndId(userId, treeId, familyId);
//        queryResultProcessor.process(familyFindResponse.getMessage(), Map.of("treeId", treeId, "familyId", familyId));
//        return familyFindResponse.getFamily();
//    }

    public void ensureFamilyExists(String familyId) {
        if (!familyRepository.existsById(familyId)) {
            throw new NodeNotFoundException("Family not found with ID: " + familyId);
        }
    }

    @TransactionalInNeo4j
    public void deleteFamily(String userId, String treeId, String familyId) {
        String result = familyRepository.delete(userId, treeId, familyId);
        queryResultProcessor.process(result, Map.of("familyId", familyId));
    }

    @TransactionalInNeo4j
    public void saveFamily(String userId, String treeId, Family family) {
        String result = familyRepository.save(userId, treeId, family.getId(), family.getStatus().name());
        queryResultProcessor.process(result, Map.of("treeId", family.getTree().getId(), "familyId", family.getId()));
    }

    @TransactionalInNeo4j
    public void updateFamily(String userId, String treeId, String familyId, Family family) {
        String result = familyRepository.update(userId, treeId, familyId, family.getStatus().name());
        queryResultProcessor.process(result, Map.of("familyId", family.getId()));
    }

    @TransactionalInNeo4j
    public void addFatherToFamily(String userId, String treeId, String familyId, String personId) {
        String result = familyRepository.addFather(userId, treeId, familyId, personId);
        queryResultProcessor.process(result, Map.of("familyId", familyId, "personId", personId));
    }

    @TransactionalInNeo4j
    public void addMotherToFamily(String userId, String treeId, String familyId, String personId) {
        String result = familyRepository.addMother(userId, treeId, familyId, personId);
        queryResultProcessor.process(result, Map.of("familyId", familyId, "personId", personId));
    }

    @TransactionalInNeo4j
    public void addChildToFamily(String userId, String treeId, String familyId, String personId, String fatherRelationshipType, String motherRelationshipType) {
        String result = familyRepository.addChild(userId, treeId, familyId, personId, fatherRelationshipType, motherRelationshipType);
        queryResultProcessor.process(result, Map.of("familyId", familyId, "personId", personId));
    }

    @TransactionalInNeo4j
    public void removeFatherFromFamily(String userId, String treeId, String familyId, String fatherId) {
        String result = familyRepository.removeFather(userId, treeId, familyId, fatherId);
        queryResultProcessor.process(result, Map.of("familyId", familyId, "fatherId", fatherId));
    }

    @TransactionalInNeo4j
    public void removeMotherFromFamily(String userId, String treeId, String familyId, String motherId) {
        String result = familyRepository.removeMother(userId, treeId, familyId, motherId);
        queryResultProcessor.process(result, Map.of("familyId", familyId, "motherId", motherId));
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(String userId, String treeId, String familyId, String childId) {
        String result = familyRepository.removeChild(userId, treeId, familyId, childId);
        queryResultProcessor.process(result, Map.of("familyId", familyId, "childId", childId));
    }

    @TransactionalInNeo4j
    public void updateChildInFamily(String userId, String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest) {
        String result = familyRepository.updateChild(userId, treeId, familyId, childId, familyChildRequest.getFatherRelationship().name(), familyChildRequest.getMotherRelationship().name());
        queryResultProcessor.process(result, Map.of("familyId", familyId, "childId", childId));
    }
}
