package com.ada.genealogyapp.file.dto.params;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetFileParams extends BaseParams {
    private String fileId;
}