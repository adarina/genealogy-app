package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.Gender;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import lombok.*;

import java.time.LocalDate;
import java.util.function.Function;


@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FamilyRequest {

    public static Function<FamilyRequest, Family> dtoToEntityMapper() {
        return request -> Family.builder()
                .build();
    }
}

