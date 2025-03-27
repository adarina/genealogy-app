package com.ada.genealogyapp.gedcom.mappers;


import com.ada.genealogyapp.person.type.GenderType;

public class GenderMapper {

    public static GenderType mapGender(String value) {
        if (value == null) {
            return GenderType.UNKNOWN;
        }
        return switch (value.toUpperCase()) {
            case "M" -> GenderType.MALE;
            case "F" -> GenderType.FEMALE;
            default -> GenderType.UNKNOWN;
        };
    }
}