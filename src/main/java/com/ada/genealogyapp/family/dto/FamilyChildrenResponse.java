package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.person.model.Person;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class FamilyChildrenResponse {

    @Singular
    private List<Child> children;

    @Getter
    @Builder
    public static class Child {

        private UUID id;

        private String firstname;

        private String lastname;

        private String birthDate;

        private String genderType;

        private List<CitationInfo> citations;

        private List<ParentsInfo> parents;

        @Getter
        @AllArgsConstructor
        public static class CitationInfo {

            private UUID id;
        }

        @Getter
        @AllArgsConstructor
        public static class ParentsInfo {

            private Long id;

            private String parentRelationshipType;
        }
    }

    public static Function<Collection<Person>, FamilyChildrenResponse> entityToDtoMapper() {
        return persons -> FamilyChildrenResponse.builder()
                .children(persons.stream()
                        .map(person -> Child.builder()
                                .id(person.getId())
                                .firstname(person.getFirstname())
                                .lastname(person.getLastname())
                                .birthDate(person.getBirthDate().toString())
                                .genderType(person.getGenderType().toString())
                                .citations(person.getCitations().stream()
                                        .map(citation -> new FamilyChildrenResponse.Child.CitationInfo(citation.getId()))
                                        .collect(Collectors.toList()))
                                .parents(person.getParents().stream()
                                        .map(parent -> new FamilyChildrenResponse.Child.ParentsInfo(parent.getId(), parent.getPersonRelationshipType().toString()))
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
