package com.ada.genealogyapp.tree.dto;

import com.ada.genealogyapp.citation.dto.CitationExportResponse;
import com.ada.genealogyapp.event.dto.EventExportResponse;
import com.ada.genealogyapp.family.dto.FamilyExportResponse;
import com.ada.genealogyapp.file.dto.FileExportResponse;
import com.ada.genealogyapp.location.dto.LocationExportResponse;
import com.ada.genealogyapp.person.dto.PersonExportResponse;
import com.ada.genealogyapp.source.dto.SourceExportResponse;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeExportJsonResponse {

    private TreeRequest tree;

    private Set<PersonExportResponse> persons;

    private Set<FamilyExportResponse> families;

    private Set<EventExportResponse> events;

    private Set<CitationExportResponse> citations;

    private Set<SourceExportResponse> sources;

    private Set<FileExportResponse> files;

    private Set<LocationExportResponse> locations;

}


