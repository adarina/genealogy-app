package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CitationManagementService {

    private final TreeService treeService;

    private final CitationService citationService;

    private final CitationRepository citationRepository;

    private final CitationValidationService citationValidationService;

    public CitationManagementService(TreeService treeService, CitationService citationService, CitationRepository citationRepository, CitationValidationService citationValidationService) {
        this.treeService = treeService;
        this.citationService = citationService;
        this.citationRepository = citationRepository;
        this.citationValidationService = citationValidationService;
    }

    @TransactionalInNeo4j
    public void updateCitation(String treeId, String citationId, CitationRequest citationRequest) {
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);

        citation.setPage(citationRequest.getPage());
        citation.setDate(citationRequest.getDate());

        citationValidationService.validateCitation(citation);

        citationService.saveCitation(citation);
    }

    @TransactionalInNeo4j
    public void deleteCitation(String treeId, String citationId) {
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);

        citationService.deleteCitation(citation);
    }
}
