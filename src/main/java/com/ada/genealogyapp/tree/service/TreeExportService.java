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
import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.*;

import java.util.Set;


@EqualsAndHashCode
@AllArgsConstructor
public abstract class TreeExportService {

    protected final PersonViewService personViewService;

    protected final FamilyViewService familyViewService;

    protected final EventViewService eventViewService;

    protected final CitationViewService citationViewService;

    protected final SourceViewService sourceViewService;

    protected final FileViewService fileViewService;

    protected final LocationViewService locationViewService;

    protected final TreeViewService treeViewService;

    public final Object exportTree(BaseParams params) {
        TreeResponse tree = fetchTree(params);

        Set<PersonExportResponse> persons = fetchPersons(params);
        Set<FamilyExportResponse> families = fetchFamilies(params);
        Set<EventExportResponse> events = fetchEvents(params);
        Set<CitationExportResponse> citations = fetchCitations(params);
        Set<SourceExportResponse> sources = fetchSources(params);
        Set<FileExportResponse> files = fetchFiles(params);
        Set<LocationExportResponse> locations = fetchLocations(params);

        return assembleOutput(tree, persons, families, events, citations, sources, files, locations, params);
    }

    protected TreeResponse fetchTree(BaseParams params) {
        return treeViewService.getTree(params);
    }

    protected Set<PersonExportResponse> fetchPersons(BaseParams params) {
        return personViewService.findPersons(params);
    }

    protected Set<FamilyExportResponse> fetchFamilies(BaseParams params) {
        return familyViewService.findFamilies(params);
    }

    protected Set<EventExportResponse> fetchEvents(BaseParams params) {
        return eventViewService.findEvents(params);
    }

    protected Set<CitationExportResponse> fetchCitations(BaseParams params) {
        return citationViewService.findCitations(params);
    }

    protected Set<SourceExportResponse> fetchSources(BaseParams params) {
        return sourceViewService.findSources(params);
    }

    protected Set<FileExportResponse> fetchFiles(BaseParams params) {
        return fileViewService.findFiles(params);
    }

    protected Set<LocationExportResponse> fetchLocations(BaseParams params) {
        return locationViewService.findLocations(params);
    }

    protected abstract Object assembleOutput(TreeResponse tree, Set<PersonExportResponse> persons,
                                             Set<FamilyExportResponse> families, Set<EventExportResponse> events,
                                             Set<CitationExportResponse> citations, Set<SourceExportResponse> sources,
                                             Set<FileExportResponse> files, Set<LocationExportResponse> locations,
                                             BaseParams params);

}
