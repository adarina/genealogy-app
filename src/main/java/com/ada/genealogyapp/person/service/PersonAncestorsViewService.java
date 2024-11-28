package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonAncestorResponse;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public PersonAncestorResponse getPersonAncestors(String treeId, String personId) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);

        Map<Person, Set<Person>> ancestorsMap = buildAncestryMap(person);
        return mapToResponse(person, ancestorsMap);
    }

    private PersonAncestorResponse mapToResponse(Person person, Map<Person, Set<Person>> ancestryMap) {
        if (person == null) return null;

        List<PersonAncestorResponse> ancestors = Optional.ofNullable(ancestryMap.get(person))
                .orElse(Collections.emptySet())
                .stream()
                .sorted((firstPerson, secondPerson) -> {
                    if (firstPerson.getGender() == GenderType.MALE && secondPerson.getGender() == GenderType.FEMALE) {
                        return -1;
                    } else if (firstPerson.getGender() == GenderType.FEMALE && secondPerson.getGender() == GenderType.MALE) {
                        return 1;
                    }
                    return 0;
                })
                .map(parent -> mapToResponse(parent, ancestryMap))
                .toList();

        return PersonAncestorResponse.builder()
                .id(person.getId())
                .name(person.getFirstname() + " " + person.getLastname())
                .gender(person.getGender().toString())
                .birthdate(person.getBirthdate() != null ? person.getBirthdate().toString() : "unknown")
                .ancestors(ancestors)
                .build();
    }

    private Map<Person, Set<Person>> buildAncestryMap(Person root) {
        Map<Person, Set<Person>> ancestors = new HashMap<>();
        buildAncestryMapRecursive(root, ancestors, new HashSet<>());
        return ancestors;
    }

    private void buildAncestryMapRecursive(Person person, Map<Person, Set<Person>> ancestors, Set<Person> visited) {
        if (person == null || visited.contains(person)) {
            return;
        }
        visited.add(person);

        Set<Person> parents = personRepository.findParentsOf(person.getId());
        if (parents.isEmpty()) {
            return;
        }

        List<Person> sortedParents = sortParents(parents);

        ancestors.put(person, new LinkedHashSet<>(sortedParents));
        for (Person parent : sortedParents) {
            buildAncestryMapRecursive(parent, ancestors, visited);
        }
    }

    private List<Person> sortParents(Set<Person> parents) {
        return parents.stream()
                .sorted((firstPerson, secondPerson) -> {
                    if (firstPerson.getGender() == GenderType.MALE && secondPerson.getGender() == GenderType.FEMALE) {
                        return -1;
                    }
                    if (firstPerson.getGender() == GenderType.FEMALE && secondPerson.getGender() == GenderType.MALE) {
                        return 1;
                    }

                    if (firstPerson.getGender() == GenderType.UNKNOWN && secondPerson.getGender() == GenderType.FEMALE) {
                        return -1;
                    }
                    if (firstPerson.getGender() == GenderType.FEMALE && secondPerson.getGender() == GenderType.UNKNOWN) {
                        return 1;
                    }

                    if (firstPerson.getGender() == GenderType.UNKNOWN && secondPerson.getGender() == GenderType.MALE) {
                        return 1;
                    }
                    if (firstPerson.getGender() == GenderType.MALE && secondPerson.getGender() == GenderType.UNKNOWN) {
                        return -1;
                    }

                    return Optional.ofNullable(firstPerson.getLastname())
                            .orElse("")
                            .compareToIgnoreCase(Optional.ofNullable(secondPerson.getLastname()).orElse(""));
                })
                .toList();
    }
}


