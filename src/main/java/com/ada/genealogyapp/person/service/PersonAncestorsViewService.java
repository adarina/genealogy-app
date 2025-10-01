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
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


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
        if (isNull(person) || visited.contains(person) || isPlaceholder(person)) {
            return;
        }
        visited.add(person);

        Set<PersonResponse> biologicalParents = personRepository.findAncestor(params.getUserId(), params.getTreeId(), person.getId());

        Set<PersonResponse> completeParents = new LinkedHashSet<>(biologicalParents);

        if (completeParents.stream().noneMatch(p -> p.getGender() == GenderType.MALE)) {
            completeParents.add(createPlaceholderPerson(GenderType.MALE));
        }

        if (completeParents.stream().noneMatch(p -> p.getGender() == GenderType.FEMALE)) {
            completeParents.add(createPlaceholderPerson(GenderType.FEMALE));
        }

        ancestors.put(person, completeParents);

        for (PersonResponse parent : completeParents) {
            buildAncestryMapRecursive(parent, ancestors, visited, params);
        }
    }

    private PersonAncestorResponse mapToResponse(PersonResponse person, Map<PersonResponse, Set<PersonResponse>> ancestryMap) {
        if (isNull(person)) {
            return null;
        }

        List<PersonAncestorResponse> ancestors = Optional.ofNullable(ancestryMap.get(person)).orElse(Collections.emptySet()).stream().sorted(this::compareByGender).map(parent -> mapToResponse(parent, ancestryMap)).collect(Collectors.toList());

        return PersonAncestorResponse.builder().id(person.getId()).name(person.getName()).gender(person.getGender() != null ? person.getGender().toString() : "UNKNOWN").birthdate(person.getBirthdate()).deathdate(person.getDeathdate()).ancestors(ancestors).build();
    }

    private PersonResponse createPlaceholderPerson(GenderType gender) {
        System.out.println("LOOOOOOOL");
        return PersonResponse.builder()
                .id("placeholder-" + UUID.randomUUID())
                .name("No data")
                .gender(gender)
                .build();
    }

    private boolean isPlaceholder(PersonResponse person) {
        return "No data".equals(person.getName());
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
