package com.ada.genealogyapp.file.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseFileParams {
    private String userId;
    private String treeId;
    private String fileId;
}