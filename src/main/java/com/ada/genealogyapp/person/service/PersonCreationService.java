package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.family.dto.params.AddChildToFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.CreateAndAddChildToFamilyParams;
import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.person.dto.params.CreateAndAddPersonToFamilyParams;
import com.ada.genealogyapp.person.dto.params.CreatePersonRequestParams;
import com.ada.genealogyapp.person.dto.params.SavePersonParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.person.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonCreationService {

    private final PersonService personService;

    private final PersonValidationService personValidationService;

    private final FamilyService familyService;

    private Person buildValidateAndSavePerson(CreatePersonRequestParams params) {
        Person person = Person.builder()
                .firstname(params.getPersonRequest().getFirstname())
                .lastname(params.getPersonRequest().getLastname())
                .gender(params.getPersonRequest().getGender())
                .build();
        personValidationService.validatePerson(person);
        personService.savePerson(SavePersonParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .personId(person.getId())
                .person(person)
                .build());
        return person;
    }

    @TransactionalInNeo4j
    public Person createPerson(CreatePersonRequestParams params) {
        return buildValidateAndSavePerson(params);
    }

    @TransactionalInNeo4j
    public void createAndAddFatherToFamily(CreateAndAddPersonToFamilyParams params) {
        Person person = buildValidateAndSavePerson(params);
        familyService.addFatherToFamily(AddPersonToFamilyParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(params.getFamilyId())
                .personId(person.getId())
                .build());
    }

    @TransactionalInNeo4j
    public void createAndAddMotherToFamily(CreateAndAddPersonToFamilyParams params) {
        Person person = buildValidateAndSavePerson(params);
        familyService.addMotherToFamily(AddPersonToFamilyParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(params.getFamilyId())
                .personId(person.getId())
                .build());
    }

    @TransactionalInNeo4j
    public void createAndAddChildToFamily(CreateAndAddChildToFamilyParams params) {
        Person person = buildValidateAndSavePerson(params);
        familyService.addChildToFamily(AddChildToFamilyRequestParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(params.getFamilyId())
                .personId(person.getId())
                .familyChildRequest(params.getFamilyChildRequest())
                .build());
    }
}
