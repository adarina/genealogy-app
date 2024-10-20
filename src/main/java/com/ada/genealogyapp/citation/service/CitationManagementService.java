package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TreeService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class CitationManagementService {

    private final TreeTransactionService treeTransactionService;

    private final TreeService treeService;

    private final CitationService citationService;

    public CitationManagementService(TreeTransactionService treeTransactionService, TreeService treeService, CitationService citationService) {
        this.treeTransactionService = treeTransactionService;
        this.treeService = treeService;
        this.citationService = citationService;
    }

    @Transactional
    public void startTransactionAndSession(UUID treeId, UUID citationId) {
        validateTreeAndCitation(treeId, citationId);
        treeTransactionService.startTransactionAndSession();
    }

    public Citation validateTreeAndCitation(UUID treeId, UUID citationId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        return citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);
    }
}
