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
public class CitationFilesResponse {

    private UUID id;

    private String name;

    private String type;

    private String path;


}