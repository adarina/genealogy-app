package com.ada.genealogyapp.family.dto.params;

import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.person.dto.params.CreateAndAddPersonToFamilyParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateAndAddChildToFamilyParams extends CreateAndAddPersonToFamilyParams {
    private FamilyChildRequest familyChildRequest;
}