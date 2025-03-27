package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.*;
import com.ada.genealogyapp.source.model.Source;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SourceManagementServiceTest {

    @Mock
    SourceService sourceService;
    @Mock
    SourceValidationService sourceValidationService;
    @InjectMocks
    SourceManagementService sourceManagementService;

    @Test
    void updateSource_shouldInvokeValidationAndService() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";

        SourceRequest sourceRequest = SourceRequest.builder()
                .name(name)
                .build();

        UpdateSourceRequestParams requestParams = UpdateSourceRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .sourceRequest(sourceRequest)
                .build();

        Source source = Source.builder()
                .name(name)
                .build();

        UpdateSourceParams params = UpdateSourceParams.builder()
                .userId(requestParams.getUserId())
                .treeId(requestParams.getTreeId())
                .sourceId(requestParams.getSourceId())
                .source(source)
                .build();

        sourceManagementService.updateSource(requestParams);

        verify(sourceValidationService).validateSource(source);
        verify(sourceService).updateSource(params);
    }

    @Test
    void deleteSource_shouldInvokeService() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        DeleteSourceParams params = DeleteSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        sourceManagementService.deleteSource(params);

        verify(sourceService).deleteSource(params);
    }
}