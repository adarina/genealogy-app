package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.person.dto.params.*;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.person.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonManagementService {

    private final PersonService personService;

    private final PersonValidationService personValidationService;

    private final FamilyService familyService;

    private void buildValidateAndUpdatePerson(UpdatePersonRequestParams params) {
        Person person = Person.builder()
                .firstname(params.getPersonRequest().getFirstname())
                .lastname(params.getPersonRequest().getLastname())
                .gender(params.getPersonRequest().getGender())
                .build();
        personValidationService.validatePerson(person);
        personService.updatePerson(UpdatePersonParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .personId(params.getPersonId())
                .person(person)
                .build());
    }

    @TransactionalInNeo4j
    public void updatePerson(UpdatePersonRequestParams params) {
        buildValidateAndUpdatePerson(params);
    }

    @TransactionalInNeo4j
    public void updateChildInFamily(UpdateChildInFamilyRequestParams params) {
        buildValidateAndUpdatePerson(params);
        familyService.updateChildInFamily(UpdateChildInFamilyParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(params.getFamilyId())
                .personId(params.getPersonId())
                .familyChildRequest(params.getFamilyChildRequest())
                .build());
    }

    @TransactionalInNeo4j
    public void deletePerson(DeletePersonParams params) {
        personService.deletePerson(params);
    }
}
