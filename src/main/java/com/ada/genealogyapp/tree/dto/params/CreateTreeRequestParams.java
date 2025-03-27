package com.ada.genealogyapp.tree.dto.params;

import com.ada.genealogyapp.tree.dto.TreeRequest;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateTreeRequestParams {
    private String userId;
    private TreeRequest treeRequest;
}
