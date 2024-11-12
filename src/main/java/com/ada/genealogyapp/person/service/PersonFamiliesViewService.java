package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.dto.PersonFamiliesResponse;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Slf4j
@Service
public class PersonFamiliesViewService {

    private final PersonManagementService personManagementService;

    private final PersonRepository personRepository;

    public PersonFamiliesViewService(PersonManagementService personManagementService, PersonRepository personRepository) {
        this.personManagementService = personManagementService;
        this.personRepository = personRepository;
    }


    public Page<PersonFamiliesResponse> getPersonalFamilies(UUID treeId, UUID personId, Pageable pageable) {
        personManagementService.validateTreeAndPerson(treeId, personId);
        return personRepository.findPersonalFamilies(personId, pageable);

    }
}