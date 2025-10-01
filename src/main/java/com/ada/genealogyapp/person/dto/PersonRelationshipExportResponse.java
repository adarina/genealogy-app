package com.ada.genealogyapp.person.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonRelationshipExportResponse {

    private String childId;

    private String relationship;
}
