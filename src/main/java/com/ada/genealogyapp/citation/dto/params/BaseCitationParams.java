package com.ada.genealogyapp.citation.dto.params;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseCitationParams {
    private String userId;
    private String treeId;
    private String citationId;
}

