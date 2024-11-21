package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitationSourceManagementService {


    private final SourceService sourceService;

    private final TreeService treeService;

    private final CitationService citationService;

    @TransactionalInNeo4j
    public void addSourceToCitation(UUID treeId, UUID citationId, UUID sourceId) {
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);
        Source source = sourceService.findSourceById(sourceId);

        if (citation.hasSource(source)) {
            throw new NodeAlreadyInNodeException("Source " + sourceId + "is already part of the citation: " + citationId);
        }
        if (nonNull(citation.getSource())) {
            removeSourceFromCitation(treeId, citationId, sourceId);
        }

        citation.addSource(source);
        citationService.saveCitation(citation);
        log.info("Source {} added successfully to the citation {}", sourceId, citationId);
    }

    @TransactionalInNeo4j
    public void removeSourceFromCitation(UUID treeId, UUID citationId, UUID sourceId) {
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);
        sourceService.ensureSourceExists(sourceId);

        citation.removeSource();
        citationService.saveCitation(citation);
        log.info("Source {} removed from citation {}", sourceId, citationId);
    }
}
