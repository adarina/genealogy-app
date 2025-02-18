package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CitationCreationService {


    private final CitationService citationService;

    private final CitationValidationService citationValidationService;

    public CitationCreationService(CitationService citationService, CitationValidationService citationValidationService) {
        this.citationService = citationService;
        this.citationValidationService = citationValidationService;
    }


    @TransactionalInNeo4j
    public Citation createCitation(String treeId, @NonNull CitationRequest citationRequest) {
        Citation citation = Citation.builder()
                .id(UUID.randomUUID().toString())
                .page(citationRequest.getPage())
                .date(citationRequest.getDate())
                .build();

        citationValidationService.validateCitation(citation);
        citationService.saveCitation(treeId, citation);
        return citation;
    }

    @TransactionalInNeo4j
    public Citation createCitationWithSourceAndEvent(Tree tree, String page, String date, String sourceId, String eventId) {
        Citation citation = Citation.builder()
                .id(UUID.randomUUID().toString())
                .tree(tree)
                .page(page)
                .date(date)
                .build();

        citationValidationService.validateCitation(citation);
        citationService.saveCitationWithSourceAndEvent(citation, sourceId, eventId);
        return citation;
    }

    @TransactionalInNeo4j
    public Citation createCitationWithSourceAndFiles(Tree tree, String page, String date, String sourceId, List<String> filesIds) {
        Citation citation = Citation.builder()
                .id(UUID.randomUUID().toString())
                .tree(tree)
                .page(page)
                .date(date)
                .build();

        citationValidationService.validateCitation(citation);
        citationService.saveCitationWithSourceAndFiles(citation, sourceId, filesIds);
        return citation;
    }
}
