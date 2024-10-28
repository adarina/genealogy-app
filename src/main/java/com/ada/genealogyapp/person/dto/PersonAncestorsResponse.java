package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.model.Person;
import lombok.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
public class PersonAncestorsResponse {

    private UUID personId;

    private String personName;

    private String birthDate;

    private List<Ancestor> ancestors;

    @Getter
    @Builder
    public static class AncestorInfo {

        private UUID id;

        private String name;

        private String birthDate;
    }

    @Getter
    @Builder
    public static class Ancestor {

        private UUID personId;

        private String personName;

        private String birthDate;

        private List<AncestorInfo> ancestors;
    }

    public static Function<Map<Person, Set<Person>>, PersonAncestorsResponse> entityToDtoMapper(UUID personId, String personName, String birthDate) {
        return ancestorsMap -> {
            PersonAncestorsResponseBuilder response = PersonAncestorsResponse.builder()
                    .personId(personId)
                    .personName(personName)
                    .birthDate(birthDate);

            List<Ancestor> ancestorsResponseList = ancestorsMap.entrySet().stream()
                    .map(entry -> {
                        Person ancestor = entry.getKey();
                        List<AncestorInfo> ancestors = entry.getValue().stream()
                                .map(ance -> new AncestorInfo(ance.getId(), ance.getName(), ance.getBirthDate().toString()))
                                .collect(Collectors.toList());

                        return Ancestor.builder()
                                .personId(ancestor.getId())
                                .personName(ancestor.getName())
                                .birthDate(ancestor.getBirthDate().toString())
                                .ancestors(ancestors)
                                .build();
                    })
                    .collect(Collectors.toList());
            response.ancestors(ancestorsResponseList);
            return response.build();
        };
    }
}
