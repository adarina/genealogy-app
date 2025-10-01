package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.location.dto.GeographyResponse;
import com.ada.genealogyapp.person.dto.PersonGeographyResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;


@Service
@Slf4j
@RequiredArgsConstructor
public class PersonGeographyViewService {

    private final PersonRepository personRepository;

    public List<GeographyResponse> getPersonGeographies(GetPersonParams params) {
        PersonGeographyResponse personGeographyResponse = personRepository.findGeography(params.getUserId(), params.getTreeId(), params.getPersonId());

        Map<PersonGeographyResponse, Set<PersonGeographyResponse>> ancestorsWithLocations = new HashMap<>();
        buildAncestryMapWithLocationsRecursive(personGeographyResponse, ancestorsWithLocations, new HashSet<>(), params);

        Map<GeographyResponse, Long> aggregatedLocations = new HashMap<>();
        collectAndCountLocations(personGeographyResponse, ancestorsWithLocations, aggregatedLocations);

        return aggregatedLocations.entrySet().stream()
                .map(entry -> {
                    GeographyResponse location = entry.getKey();
                    location.setAmount(entry.getValue());
                    return location;
                })
                .toList();
    }

    private void collectAndCountLocations(PersonGeographyResponse person, Map<PersonGeographyResponse, Set<PersonGeographyResponse>> ancestryMap, Map<GeographyResponse, Long> aggregatedLocations) {
        if (nonNull(person.getLocations())) {
            for (GeographyResponse geographyResponse : person.getLocations()) {
                if (nonNull(geographyResponse.getId())) {
                    GeographyResponse location = GeographyResponse.builder()
                            .id(geographyResponse.getId())
                            .name(geographyResponse.getName())
                            .latitude(geographyResponse.getLatitude())
                            .longitude(geographyResponse.getLongitude())
                            .build();
                    aggregatedLocations.put(location, aggregatedLocations.getOrDefault(location, 0L) + 1);
                }
            }
        }

        Set<PersonGeographyResponse> parents = ancestryMap.get(person);
        if (nonNull(parents)) {
            for (PersonGeographyResponse parent : parents) {
                collectAndCountLocations(parent, ancestryMap, aggregatedLocations);
            }
        }
    }

    private void buildAncestryMapWithLocationsRecursive(PersonGeographyResponse person, Map<PersonGeographyResponse, Set<PersonGeographyResponse>> ancestors, Set<PersonGeographyResponse> visited, GetPersonParams params) {
        if (person == null || visited.contains(person)) {
            return;
        }
        visited.add(person);

        Set<PersonGeographyResponse> parents = personRepository.findAncestorGeography(params.getUserId(), params.getTreeId(), person.getId());
        if (parents.isEmpty()) {
            return;
        }

        ancestors.put(person, new LinkedHashSet<>(parents));
        for (PersonGeographyResponse parent : parents) {
            buildAncestryMapWithLocationsRecursive(parent, ancestors, visited, params);
        }
    }
}
