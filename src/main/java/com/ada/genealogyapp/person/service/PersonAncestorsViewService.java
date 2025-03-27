package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.dto.PersonAncestorResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.repository.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

//TODO performance, change to one query someday
@Service
@Slf4j
@RequiredArgsConstructor
public class PersonAncestorsViewService {

    private final PersonRepository personRepository;

    public PersonAncestorResponse getPersonAncestors(GetPersonParams params) {
        PersonResponse person = personRepository.find(params.getUserId(), params.getTreeId(), params.getPersonId());

        Map<PersonResponse, Set<PersonResponse>> ancestors = new HashMap<>();
        buildAncestryMapRecursive(person, ancestors, new HashSet<>(), params);
        return mapToResponse(person, ancestors);
    }

    private void buildAncestryMapRecursive(PersonResponse person, Map<PersonResponse, Set<PersonResponse>> ancestors, Set<PersonResponse> visited, GetPersonParams params) {
        if (person == null || visited.contains(person)) {
            return;
        }
        visited.add(person);

        Set<PersonResponse> parents = personRepository.findParentsOf(params.getUserId(), params.getTreeId(), person.getId());
        if (parents.isEmpty()) {
            return;
        }

        ancestors.put(person, new LinkedHashSet<>(parents));
        for (PersonResponse parent : parents) {
            buildAncestryMapRecursive(parent, ancestors, visited, params);
        }
    }

    private PersonAncestorResponse mapToResponse(PersonResponse person, Map<PersonResponse, Set<PersonResponse>> ancestryMap) {
        if (person == null) return null;

        List<PersonAncestorResponse> ancestors = Optional.ofNullable(ancestryMap.get(person))
                .orElse(Collections.emptySet())
                .stream()
                .sorted(this::compareByGender)
                .map(parent -> mapToResponse(parent, ancestryMap))
                .toList();

        return PersonAncestorResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .gender(person.getGender().toString())
                .birthdate(person.getBirthdate())
                .deathdate(person.getDeathdate())
                .ancestors(ancestors)
                .build();
    }

    private int compareByGender(PersonResponse firstPerson, PersonResponse secondPerson) {
        if (firstPerson.getGender() == secondPerson.getGender()) {
            return 0;
        }
        if (firstPerson.getGender() == GenderType.MALE) {
            return -1;
        }
        if (firstPerson.getGender() == GenderType.FEMALE) {
            return secondPerson.getGender() == GenderType.MALE ? 1 : -1;
        }
        return 1;
    }
}