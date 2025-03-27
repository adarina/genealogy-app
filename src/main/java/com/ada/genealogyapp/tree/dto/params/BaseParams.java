package com.ada.genealogyapp.tree.dto.params;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseParams {
    private String userId;
    private String treeId;
}
