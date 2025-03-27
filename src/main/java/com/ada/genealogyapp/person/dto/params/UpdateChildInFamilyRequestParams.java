package com.ada.genealogyapp.person.dto.params;

import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateChildInFamilyRequestParams extends UpdatePersonRequestParams {
    private String familyId;
    private FamilyChildRequest familyChildRequest;
}