package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationExportResponse;
import com.ada.genealogyapp.citation.service.CitationViewService;
import com.ada.genealogyapp.event.dto.EventExportResponse;
import com.ada.genealogyapp.event.service.EventViewService;
import com.ada.genealogyapp.family.dto.FamilyExportResponse;
import com.ada.genealogyapp.family.service.FamilyViewService;
import com.ada.genealogyapp.file.dto.FileExportResponse;
import com.ada.genealogyapp.file.service.FileViewService;
import com.ada.genealogyapp.location.dto.LocationExportResponse;
import com.ada.genealogyapp.location.service.LocationViewService;
import com.ada.genealogyapp.person.dto.PersonExportResponse;
import com.ada.genealogyapp.person.service.PersonViewService;
import com.ada.genealogyapp.source.dto.SourceExportResponse;
import com.ada.genealogyapp.source.service.SourceViewService;
import com.ada.genealogyapp.tree.dto.TreeExportJsonResponse;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Getter
@EqualsAndHashCode(callSuper = true)
public class TreeExportJsonService extends TreeExportService {

    public TreeExportJsonService(PersonViewService personViewService, FamilyViewService familyViewService,
                                 EventViewService eventViewService, CitationViewService citationViewService,
                                 SourceViewService sourceViewService, FileViewService fileViewService,
                                 LocationViewService locationViewService, TreeViewService treeViewService) {
        super(personViewService, familyViewService, eventViewService, citationViewService, sourceViewService,
                fileViewService, locationViewService, treeViewService);
    }

    @Override
    protected Object assembleOutput(TreeResponse tree, Set<PersonExportResponse> persons, Set<FamilyExportResponse> families,
                                    Set<EventExportResponse> events, Set<CitationExportResponse> citations,
                                    Set<SourceExportResponse> sources, Set<FileExportResponse> files,
                                    Set<LocationExportResponse> locations, BaseParams params) {

        TreeRequest treeRequest = TreeRequest.builder()
                .id(tree.getId())
                .name(tree.getName())
                .build();

        return TreeExportJsonResponse.builder()
                .tree(treeRequest)
                .persons(persons)
                .families(families)
                .events(events)
                .citations(citations)
                .sources(sources)
                .files(files)
                .locations(locations)
                .build();
    }
}
