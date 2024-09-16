package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.Gender;
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

    private LocalDate birthDate;

    private Gender gender;


    public static Function<PersonRequest, Person> dtoToEntityMapper() {
        return request -> Person.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .build();
    }
}