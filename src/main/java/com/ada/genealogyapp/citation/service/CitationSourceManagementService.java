package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.params.AddSourceToCitationParams;
import com.ada.genealogyapp.citation.dto.params.RemoveSourceFromCitationParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitationSourceManagementService {

    private final CitationService citationService;

    @TransactionalInNeo4j
    public void addSourceToCitation(AddSourceToCitationParams params) {
        citationService.addSourceToCitation(params);
    }

    @TransactionalInNeo4j
    public void removeSourceFromCitation(RemoveSourceFromCitationParams params) {
        citationService.removeSourceFromCitation(params);
    }
}
