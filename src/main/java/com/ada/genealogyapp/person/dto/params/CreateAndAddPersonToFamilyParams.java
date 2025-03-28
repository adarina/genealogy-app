package com.ada.genealogyapp.person.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateAndAddPersonToFamilyParams extends CreatePersonRequestParams {
    private String familyId;
}