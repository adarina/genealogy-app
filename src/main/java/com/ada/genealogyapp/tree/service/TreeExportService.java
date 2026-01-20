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
import java.util.concurrent.*;


@EqualsAndHashCode
@AllArgsConstructor
public abstract class TreeExportService {

    private final ExecutorService exportExecutor = new ThreadPoolExecutor(
            4, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
    );

    protected final PersonViewService personViewService;

    protected final FamilyViewService familyViewService;

    protected final EventViewService eventViewService;

    protected final CitationViewService citationViewService;

    protected final SourceViewService sourceViewService;

    protected final FileViewService fileViewService;

    protected final LocationViewService locationViewService;

    protected final TreeViewService treeViewService;

    public final Object exportTree(BaseParams params) {
        CompletableFuture<TreeResponse> treeFuture = CompletableFuture.supplyAsync(() -> fetchTree(params), exportExecutor);
        CompletableFuture<Set<PersonExportResponse>> personsFuture = CompletableFuture.supplyAsync(() -> fetchPersons(params), exportExecutor);
        CompletableFuture<Set<FamilyExportResponse>> familiesFuture = CompletableFuture.supplyAsync(() -> fetchFamilies(params), exportExecutor);
        CompletableFuture<Set<EventExportResponse>> eventsFuture = CompletableFuture.supplyAsync(() -> fetchEvents(params), exportExecutor);
        CompletableFuture<Set<CitationExportResponse>> citationsFuture = CompletableFuture.supplyAsync(() -> fetchCitations(params), exportExecutor);
        CompletableFuture<Set<SourceExportResponse>> sourcesFuture = CompletableFuture.supplyAsync(() -> fetchSources(params), exportExecutor);
        CompletableFuture<Set<FileExportResponse>> filesFuture = CompletableFuture.supplyAsync(() -> fetchFiles(params), exportExecutor);
        CompletableFuture<Set<LocationExportResponse>> locationsFuture = CompletableFuture.supplyAsync(() -> fetchLocations(params), exportExecutor);

        CompletableFuture.allOf(treeFuture, personsFuture, familiesFuture, eventsFuture, citationsFuture, sourcesFuture, filesFuture, locationsFuture).join();

        TreeResponse tree = treeFuture.join();
        Set<PersonExportResponse> persons = personsFuture.join();
        Set<FamilyExportResponse> families = familiesFuture.join();
        Set<EventExportResponse> events = eventsFuture.join();
        Set<CitationExportResponse> citations = citationsFuture.join();
        Set<SourceExportResponse> sources = sourcesFuture.join();
        Set<FileExportResponse> files = filesFuture.join();
        Set<LocationExportResponse> locations = locationsFuture.join();

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
