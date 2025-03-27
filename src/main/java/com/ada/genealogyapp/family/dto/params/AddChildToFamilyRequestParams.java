package com.ada.genealogyapp.family.dto.params;

import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddChildToFamilyRequestParams extends AddPersonToFamilyParams {
    private FamilyChildRequest familyChildRequest;
}