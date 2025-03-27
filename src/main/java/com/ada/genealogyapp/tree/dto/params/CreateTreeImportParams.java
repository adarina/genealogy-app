package com.ada.genealogyapp.tree.dto.params;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTreeImportParams {
    private String userId;
    private String name;
}
