package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class LineageService {

    private final PersonRepository personRepository;

    public LineageService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void printAncestorsTree(String firstname, String lastname) {
        Person person = personRepository.findByFirstnameAndLastname(firstname, lastname);
        if (person == null) {
            System.out.println("Person not found");
            return;
        }
        Map<Person, Set<Person>> ancestryMap = buildAncestryMap(person);
        printTree(person, ancestryMap, "", true);
    }

    private Map<Person, Set<Person>> buildAncestryMap(Person root) {
        Map<Person, Set<Person>> ancestryMap = new HashMap<>();
        buildAncestryMapRecursive(root, ancestryMap);
        return ancestryMap;
    }

    private void buildAncestryMapRecursive(Person person, Map<Person, Set<Person>> ancestryMap) {
        if (person == null) return;

        Set<Person> parents = findParents(person);
        if (parents.isEmpty()) {
            return;
        }

        ancestryMap.put(person, parents);

        for (Person parent : parents) {
            buildAncestryMapRecursive(parent, ancestryMap);
        }
    }

    private Set<Person> findParents(Person person) {
        return personRepository.findParentsOf(person.getId());
    }

    private void printTree(Person person, Map<Person, Set<Person>> ancestryMap, String prefix, boolean isTail) {
        if (person == null) return;

        System.out.println(prefix + (isTail ? "└── " : "├── ") + person.getFirstname() + person.getLastname() + " (Born: " + (person.getBirthDate() != null ? person.getBirthDate().toString() : "unknown") + ")");

        Set<Person> parents = ancestryMap.get(person);
        if (parents != null) {
            int count = 0;
            for (Person parent : parents) {
                printTree(parent, ancestryMap, prefix + (isTail ? "    " : "│   "), ++count == parents.size());
            }
        }
    }
}

