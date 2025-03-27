package com.ada.genealogyapp.citation.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public class CitationResponse {

    private String id;

    private String page;

    private String date;

}
