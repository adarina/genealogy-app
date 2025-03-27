package com.ada.genealogyapp.file.dto.params;

import com.ada.genealogyapp.family.dto.params.BaseFamilyParams;
import com.ada.genealogyapp.file.model.File;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaveFileParams extends BaseFileParams {
    private File file;
}