package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FamilyManagementService {

    private final FamilyRepository familyRepository;

    private final PersonService personService;

    public FamilyManagementService(FamilyRepository familyRepository, PersonService personService) {
        this.familyRepository = familyRepository;
        this.personService = personService;
    }

    public void addPersonToFamily(UUID familyId, PersonRequest personRequest) {
        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);

        Optional<Family> existingFamily = familyRepository.findById(familyId);

        if (existingFamily.isEmpty()) {
            log.error("Family with ID {} does not exist", familyId);
            throw new NodeNotFoundException("Family with ID " + familyId + " does not exist");
        }

        person = personService.create(person);

        Family family = existingFamily.get();
        family.addMember(person);

        familyRepository.save(family);

        log.info("Person added to family successfully: {}", person.getFirstname());

    }

}
