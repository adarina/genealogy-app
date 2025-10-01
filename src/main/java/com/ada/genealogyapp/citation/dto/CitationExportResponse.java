package com.ada.genealogyapp.citation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitationExportResponse {

    private String page;

    private String date;

    private String id;

    private String sourceId;

    private Set<String> filesIds = new HashSet<>();
}
