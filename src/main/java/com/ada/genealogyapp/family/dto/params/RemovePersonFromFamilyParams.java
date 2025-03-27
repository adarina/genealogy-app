package com.ada.genealogyapp.family.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RemovePersonFromFamilyParams extends BaseFamilyParams {
    private String personId;
}