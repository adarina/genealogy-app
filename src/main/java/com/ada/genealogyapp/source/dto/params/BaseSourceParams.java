package com.ada.genealogyapp.source.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseSourceParams {
    private String userId;
    private String treeId;
    private String sourceId;
}