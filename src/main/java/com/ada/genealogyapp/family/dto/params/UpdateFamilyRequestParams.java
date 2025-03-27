package com.ada.genealogyapp.family.dto.params;


import com.ada.genealogyapp.family.dto.FamilyRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateFamilyRequestParams extends BaseFamilyParams {
    private FamilyRequest familyRequest;
}