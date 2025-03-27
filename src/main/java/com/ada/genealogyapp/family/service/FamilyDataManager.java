package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.family.dto.params.*;
import com.ada.genealogyapp.person.dto.params.UpdateChildInFamilyParams;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyDataManager implements FamilyService {


    private final FamilyRepository familyRepository;

    private final QueryResultProcessor processor;


    @TransactionalInNeo4j
    public void deleteFamily(DeleteFamilyParams params) {
        String result = familyRepository.delete(params.getUserId(), params.getTreeId(), params.getFamilyId());
        processor.process(result, Map.of(IdType.FAMILY_ID,  params.getFamilyId()));
    }

    @TransactionalInNeo4j
    public void saveFamily(SaveFamilyParams params) {
        String result = familyRepository.save(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getFamily().getStatus().name());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.FAMILY_ID, params.getFamilyId()));
    }

    @TransactionalInNeo4j
    public void updateFamily(UpdateFamilyParams params) {
        String result = familyRepository.update(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getFamily().getStatus().name());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId()));
    }

    @TransactionalInNeo4j
    public void addFatherToFamily(AddPersonToFamilyParams params) {
        String result = familyRepository.addFather(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.PERSON_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void addMotherToFamily(AddPersonToFamilyParams params) {
        String result = familyRepository.addMother(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.PERSON_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void addChildToFamily(AddChildToFamilyRequestParams params) {
        String result = familyRepository.addChild(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId(), params.getFamilyChildRequest().getFatherRelationship().name(), params.getFamilyChildRequest().getMotherRelationship().name());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.PERSON_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void removeFatherFromFamily(RemovePersonFromFamilyParams params) {
        String result = familyRepository.removeFather(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.FATHER_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void removeMotherFromFamily(RemovePersonFromFamilyParams params) {
        String result = familyRepository.removeMother(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.MOTHER_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(RemovePersonFromFamilyParams params) {
        String result = familyRepository.removeChild(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.CHILD_ID, params.getPersonId()));
    }

    @TransactionalInNeo4j
    public void updateChildInFamily(UpdateChildInFamilyParams params) {
        String result = familyRepository.updateChild(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPersonId(), params.getFamilyChildRequest().getFatherRelationship().name(), params.getFamilyChildRequest().getMotherRelationship().name());
        processor.process(result, Map.of(IdType.FAMILY_ID, params.getFamilyId(), IdType.CHILD_ID, params.getPersonId()));
    }
}
