package com.ada.genealogyapp.citation.dto.params;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateCitationRequestParams extends BaseParams {
    private CitationRequest citationRequest;
}
