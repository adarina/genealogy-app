package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.dto.params.DeleteSourceParams;
import com.ada.genealogyapp.source.dto.params.UpdateSourceParams;
import com.ada.genealogyapp.source.dto.params.UpdateSourceRequestParams;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SourceManagementService {

    private final SourceService sourceService;

    private final SourceValidationService sourceValidationService;

    @TransactionalInNeo4j
    public void updateSource(UpdateSourceRequestParams params) {
        Source source = Source.builder()
                .name(params.getSourceRequest().getName())
                .build();

        sourceValidationService.validateSource(source);
        sourceService.updateSource(UpdateSourceParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .sourceId(params.getSourceId())
                .source(source)
                .build());
    }

    @TransactionalInNeo4j
    public void deleteSource(DeleteSourceParams params) {
        sourceService.deleteSource(params);
    }
}
