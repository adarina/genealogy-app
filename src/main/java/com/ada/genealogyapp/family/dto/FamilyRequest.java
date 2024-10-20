package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.type.FamilyRelationshipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class FamilyRequest {

    public FamilyRelationshipType familyRelationshipType;

}
