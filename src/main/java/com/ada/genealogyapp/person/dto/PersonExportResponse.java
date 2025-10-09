package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonExportResponse {

    private String id;

    private String firstname;

    private String lastname;

    private String name;

    private GenderType gender;

    private Set<PersonRelationshipExportResponse> relationships;
}
