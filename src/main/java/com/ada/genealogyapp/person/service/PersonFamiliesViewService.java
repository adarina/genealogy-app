package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.dto.PersonFamilyResponse;
import com.ada.genealogyapp.person.repository.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonFamiliesViewService {

    private final TreeService treeService;

    private final PersonService personService;

    private final PersonRepository personRepository;

    public PersonFamiliesViewService(TreeService treeService, PersonService personService, PersonRepository personRepository) {
        this.treeService = treeService;
        this.personService = personService;
        this.personRepository = personRepository;
    }


    public Page<PersonFamilyResponse> getPersonalFamilies(String treeId, String personId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        personService.ensurePersonExists(personId);
        return personRepository.findPersonalFamilies(personId, pageable);

    }
}