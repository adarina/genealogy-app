package com.ada.genealogyapp.citation.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CitationSourceResponse extends CitationResponse {

    private String name;

    private String sourceId;
}
