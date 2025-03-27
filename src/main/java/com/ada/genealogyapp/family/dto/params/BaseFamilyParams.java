package com.ada.genealogyapp.family.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseFamilyParams {
    private String userId;
    private String treeId;
    private String familyId;
}