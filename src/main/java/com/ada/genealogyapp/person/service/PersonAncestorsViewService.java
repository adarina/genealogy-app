package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonAncestorsResponse;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class PersonAncestorsViewService {

    private final PersonRepository personRepository;

    private final PersonService personService;
    private final TreeService treeService;

    public PersonAncestorsViewService(PersonRepository personRepository, PersonService personService, TreeService treeService) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.treeService = treeService;
    }

    public PersonAncestorsResponse getPersonAncestors(UUID treeId, UUID personId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        Person person = personService.findPersonByIdOrThrowNodeNotFoundException(personId);

        Map<Person, Set<Person>> ancestorsMap = buildAncestryMap(person);
        return PersonAncestorsResponse.entityToDtoMapper(person.getId(), person.getName(), person.getBirthdate().toString()).apply(ancestorsMap);
    }

    private Map<Person, Set<Person>> buildAncestryMap(Person root) {
        Map<Person, Set<Person>> ancestors = new HashMap<>();
        buildAncestryMapRecursive(root, ancestors);
        return ancestors;
    }

    private void buildAncestryMapRecursive(Person person, Map<Person, Set<Person>> ancestors) {
        if (person == null) return;

        Set<Person> parents = personRepository.findParentsOf(person.getId());
        if (parents.isEmpty()) {
            return;
        }

        ancestors.put(person, parents);
        for (Person parent : parents) {
            buildAncestryMapRecursive(parent, ancestors);
        }
    }
}


