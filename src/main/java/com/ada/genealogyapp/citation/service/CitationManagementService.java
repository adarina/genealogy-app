package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
public class CitationManagementService {

    private final TreeService treeService;

    private final CitationService citationService;

    private final CitationRepository citationRepository;

    public CitationManagementService(TreeService treeService, CitationService citationService, CitationRepository citationRepository) {
        this.treeService = treeService;
        this.citationService = citationService;
        this.citationRepository = citationRepository;
    }

    //TODO validation
    @TransactionalInNeo4j
    public void updateCitation(UUID treeId, UUID citationId, CitationRequest citationRequest) {
        treeService.ensureTreeExists(treeId);
        citationService.ensureCitationExists(citationId);

        citationRepository.updateCitation(citationId, citationRequest.getPage(), citationRequest.getDate());
        log.info("Citation updated successfully: {}", citationId);
    }
}
