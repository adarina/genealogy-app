package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.params.*;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.dto.params.AddCitationToEventParams;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitationCreationService {

    private final CitationService citationService;

    private final CitationValidationService citationValidationService;

    private final EventService eventService;

    private Citation buildAndValidateCitation(CreateCitationRequestParams params) {
        Citation citation = Citation.builder()
                .id(UUID.randomUUID().toString())
                .page(params.getCitationRequest().getPage())
                .date(params.getCitationRequest().getDate())
                .build();
        citationValidationService.validateCitation(citation);
        return citation;
    }

    private Citation buildValidateAndSaveCitation(CreateCitationRequestParams params) {
        Citation citation = buildAndValidateCitation(params);
        citationService.saveCitation(SaveCitationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(citation.getId())
                .citation(citation)
                .build());
        return citation;
    }

    @TransactionalInNeo4j
    public void createCitation(CreateCitationRequestParams params) {
        buildValidateAndSaveCitation(params);
    }

    @TransactionalInNeo4j
    public Citation createCitationWithSourceAndEvent(CreateCitationWithSourceAndEventParams params) {
        Citation citation = buildAndValidateCitation(params);
        citationService.saveCitationWithSourceAndEvent(SaveCitationWithSourceAndEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(citation.getId())
                .citation(citation)
                .sourceId(params.getSourceId())
                .eventId(params.getEventId())
                .build());
        return citation;
    }

    @TransactionalInNeo4j
    public Citation createCitationWithSourceAndFiles(CreateCitationWithSourceAndFilesParams params) {
        Citation citation = buildAndValidateCitation(params);
        citationService.saveCitationWithSourceAndFiles(SaveCitationWithSourceAndFilesParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(citation.getId())
                .citation(citation)
                .sourceId(params.getSourceId())
                .filesIds(params.getFilesIds())
                .build());
        return citation;
    }

    @TransactionalInNeo4j
    public void createAndAddCitationToEvent(CreateAndAddCitationToEventRequestParams params) {
        Citation citation = buildValidateAndSaveCitation(params);
        eventService.addCitationToEvent(AddCitationToEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(params.getEventId())
                .citationId(citation.getId())
                .build());
    }
}
