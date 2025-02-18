package com.ada.genealogyapp.citation.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CitationJsonRequest extends CitationRequest {

    private String id;

    private String sourceId;

    private List<String> filesIds = new ArrayList<>();
}
