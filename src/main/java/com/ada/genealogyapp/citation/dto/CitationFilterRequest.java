package com.ada.genealogyapp.citation.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CitationFilterRequest {

    private String page;

    private String name;

}
