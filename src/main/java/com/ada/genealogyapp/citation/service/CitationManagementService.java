package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CitationManagementService {

    private final CitationService citationService;

    private final CitationValidationService citationValidationService;

    public CitationManagementService(CitationService citationService, CitationValidationService citationValidationService) {
        this.citationService = citationService;
        this.citationValidationService = citationValidationService;
    }

    @TransactionalInNeo4j
    public void updateCitation(String treeId, String citationId, CitationRequest citationRequest) {
        Citation citation = Citation.builder()
                .page(citationRequest.getPage())
                .date(citationRequest.getDate())
                .build();

        citationValidationService.validateCitation(citation);
        citationService.updateCitation(treeId, citationId, citation);
    }

    @TransactionalInNeo4j
    public void deleteCitation(String treeId, String citationId) {
        citationService.deleteCitation(treeId, citationId);
    }
}