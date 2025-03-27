package com.ada.genealogyapp.tree.dto.params;

import com.ada.genealogyapp.tree.dto.TreeRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateTreeRequestParams extends BaseParams {
    private TreeRequest treeRequest;
}
