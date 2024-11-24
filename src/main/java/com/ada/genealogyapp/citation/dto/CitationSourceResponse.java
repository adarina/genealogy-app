package com.ada.genealogyapp.citation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitationSourceResponse extends CitationResponse {

    private String name;

    private UUID sourceId;
}
