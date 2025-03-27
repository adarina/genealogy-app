package com.ada.genealogyapp.citation.dto.params;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateCitationRequestParams extends BaseCitationParams {
    private CitationRequest citationRequest;
}