package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.citation.dto.params.AddSourceToCitationParams;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.source.dto.params.CreateAndAddSourceToCitationRequestParams;
import com.ada.genealogyapp.source.dto.params.CreateSourceRequestParams;
import com.ada.genealogyapp.source.dto.params.SaveSourceParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.source.model.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SourceCreationService {

    private final SourceService sourceService;

    private final SourceValidationService sourceValidationService;

    private final CitationService citationService;

    public Source buildValidateAndSaveSource(CreateSourceRequestParams params) {
        Source source = Source.builder()
                .id(UUID.randomUUID().toString())
                .name(params.getSourceRequest().getName())
                .build();

        sourceValidationService.validateSource(source);
        sourceService.saveSource(SaveSourceParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .sourceId(source.getId())
                .source(source)
                .build());
        return source;
    }

    @TransactionalInNeo4j
    public Source createSource(CreateSourceRequestParams params) {
        return buildValidateAndSaveSource(params);
    }

    @TransactionalInNeo4j
    public void createAndAddSourceToCitation(CreateAndAddSourceToCitationRequestParams params) {
        Source source = buildValidateAndSaveSource(params);
        citationService.addSourceToCitation(AddSourceToCitationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(params.getCitationId())
                .sourceId(source.getId())
                .build());
    }
}
