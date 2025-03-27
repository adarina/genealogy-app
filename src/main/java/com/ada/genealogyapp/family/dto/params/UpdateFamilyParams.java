package com.ada.genealogyapp.family.dto.params;


import com.ada.genealogyapp.family.model.Family;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateFamilyParams extends BaseFamilyParams {
    private Family family;
}