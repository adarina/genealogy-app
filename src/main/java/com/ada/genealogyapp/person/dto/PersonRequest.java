package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.model.Person;
import lombok.*;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PersonRequest {

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    private GenderType gender;


    public static Function<PersonRequest, Person> dtoToEntityMapper() {
        return request -> Person.builder()
                .name(request.getFirstname() + " " + request.getLastname())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .birthdate(request.getBirthdate())
                .gender(request.getGender())
                .build();
    }
}