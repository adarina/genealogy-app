package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class PersonFamiliesResponse {

    @Getter
    @Builder
    public static class Family {

        private UUID id;

        private String father;

        private UUID fatherId;

        private String fatherBirthdate;

        private String mother;

        private UUID motherId;

        private String motherBirthdate;

        private List<ChildInfo> children;


        @Getter
        @AllArgsConstructor
        public static class ChildInfo {

            private UUID childId;

            private String childName;

            private String childBirthdate;
        }
    }

    @Singular
    private List<Family> families;

    public static Function<Collection<FamilyRelationship>, PersonFamiliesResponse> entityToDtoMapper() {
        return familyRelationships -> {
            PersonFamiliesResponseBuilder response = PersonFamiliesResponse.builder();

            familyRelationships.stream()
                    .map(familyRelationship -> {
                        com.ada.genealogyapp.family.model.Family family = familyRelationship.getFamily();

                        return Family.builder()
                                .id(family.getId())
                                .fatherId(family.getFather().getId())
                                .father(family.getFather().getName())
                                .fatherBirthdate(family.getFather().getBirthDate().toString())
                                .motherId(family.getMother().getId())
                                .mother(family.getMother().getName())
                                .motherBirthdate(family.getMother().getBirthDate().toString())
                                .children(family.getChildren().stream()
                                        .map(child -> new Family.ChildInfo(child.getId(), child.getName(), child.getBirthDate().toString()))
                                        .collect(Collectors.toList()))
                                .build();
                    })
                    .forEach(response::family);
            return response.build();
        };
    }
}