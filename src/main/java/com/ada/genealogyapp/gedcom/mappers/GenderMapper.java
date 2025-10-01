package com.ada.genealogyapp.gedcom.mappers;


import com.ada.genealogyapp.gedcom.type.PersonGedcomType;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.dto.gedcom.PersonGedcomRequest;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class GenderMapper {

    private static final Map<GenderType, PersonGedcomType> genderTypeToGedcomTagMap = new HashMap<>();
    private static final Map<PersonGedcomType, GenderType> gedcomTagToGenderTypeMap = new HashMap<>();

    static {
        genderTypeToGedcomTagMap.put(GenderType.MALE, PersonGedcomType.M);
        genderTypeToGedcomTagMap.put(GenderType.FEMALE, PersonGedcomType.F);
        genderTypeToGedcomTagMap.put(GenderType.UNKNOWN, PersonGedcomType.U);

        gedcomTagToGenderTypeMap.put(PersonGedcomType.M, GenderType.MALE);
        gedcomTagToGenderTypeMap.put(PersonGedcomType.F, GenderType.FEMALE);
        gedcomTagToGenderTypeMap.put(PersonGedcomType.U, GenderType.UNKNOWN);
    }

    public static PersonGedcomType getGedcomGenderTag(GenderType genderType) {
        return genderTypeToGedcomTagMap.get(genderType);
    }

    public static GenderType mapGender(PersonGedcomType gedcomType) {
        if (isNull(gedcomType)) {
            return GenderType.UNKNOWN;
        }
        return gedcomTagToGenderTypeMap.getOrDefault(gedcomType, GenderType.UNKNOWN);
    }

    public static GenderType extractGender(PersonGedcomRequest personRequest) {
        return Option.of(personRequest.getEventsFacts())
                .filter(facts -> !facts.isEmpty())
                .map(facts -> facts.get(0).getValue())
                .flatMap(value -> Try.of(() -> mapGender(PersonGedcomType.valueOf(value)))
                        .toOption())
                .getOrElse(GenderType.UNKNOWN);
    }
}
