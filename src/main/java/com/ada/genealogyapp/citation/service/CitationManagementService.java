package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.params.DeleteCitationParams;
import com.ada.genealogyapp.citation.dto.params.UpdateCitationParams;
import com.ada.genealogyapp.citation.dto.params.UpdateCitationRequestParams;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CitationManagementService {

    private final CitationService citationService;

    private final CitationValidationService citationValidationService;

    @TransactionalInNeo4j
    public void updateCitation(UpdateCitationRequestParams params) {
        Citation citation = Citation.builder()
                .page(params.getCitationRequest().getPage())
                .date(params.getCitationRequest().getDate())
                .build();

        citationValidationService.validateCitation(citation);
        citationService.updateCitation(UpdateCitationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(params.getCitationId())
                .citation(citation)
                .build());
    }

    @TransactionalInNeo4j
    public void deleteCitation(DeleteCitationParams params) {
        citationService.deleteCitation(params);
    }
}