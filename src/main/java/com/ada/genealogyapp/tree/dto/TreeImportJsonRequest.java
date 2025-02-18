package com.ada.genealogyapp.tree.dto;

import com.ada.genealogyapp.citation.dto.CitationJsonRequest;
import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.family.dto.FamilyJsonRequest;
import com.ada.genealogyapp.file.dto.FileJsonRequest;
import com.ada.genealogyapp.person.dto.PersonJsonRequest;
import com.ada.genealogyapp.source.dto.SourceJsonRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TreeImportJsonRequest {

    private TreeRequest tree;

    private List<PersonJsonRequest> persons;

    private List<FamilyJsonRequest> families;

    private List<EventJsonRequest> events;

    private List<CitationJsonRequest> citations;

    private List<SourceJsonRequest> sources;

    private List<FileJsonRequest> files;

}