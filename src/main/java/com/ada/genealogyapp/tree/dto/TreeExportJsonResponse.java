package com.ada.genealogyapp.tree.dto;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.file.dto.MultipartFileRequest;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.source.dto.SourceRequest;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeExportJsonResponse {

    private TreeRequest tree;

    private List<PersonRequest> persons;

    private List<FamilyRequest> families;

    private List<EventRequest> events;

    private List<CitationRequest> citations;

    private List<SourceRequest> sources;

    private List<MultipartFileRequest> files;
}


