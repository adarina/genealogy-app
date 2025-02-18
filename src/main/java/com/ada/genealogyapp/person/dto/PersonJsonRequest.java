package com.ada.genealogyapp.person.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonJsonRequest extends PersonRequest {

    private String id;

    private String name;

    private List<PersonRelationshipRequest> relationships;

}
