package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.CreateSourceRequestParams;
import com.ada.genealogyapp.source.dto.params.SaveSourceParams;
import com.ada.genealogyapp.source.model.Source;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SourceCreationServiceTest {

    @Mock
    SourceService sourceService;
    @Mock
    SourceValidationService sourceValidationService;
    @InjectMocks
    SourceCreationService sourceCreationService;

    @Test
    void createSource_shouldBuildValidateAndSaveSource() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceName = "book123";

        SourceRequest sourceRequest = SourceRequest.builder()
                .name(sourceName)
                .build();

        CreateSourceRequestParams params = CreateSourceRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceRequest(sourceRequest)
                .build();

        doNothing().when(sourceValidationService).validateSource(any(Source.class));
        doNothing().when(sourceService).saveSource(any(SaveSourceParams.class));

        Source createdSource = sourceCreationService.createSource(params);

        assertNotNull(createdSource);
        assertEquals(sourceName, createdSource.getName());

        verify(sourceValidationService).validateSource(any(Source.class));
        verify(sourceService).saveSource(any(SaveSourceParams.class));
    }

    @Test
    void createSource_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceName = "book123";

        SourceRequest sourceRequest = SourceRequest.builder()
                .name(sourceName)
                .build();

        CreateSourceRequestParams params = CreateSourceRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceRequest(sourceRequest)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(sourceService).saveSource(any(SaveSourceParams.class));

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class,
                () -> sourceCreationService.createSource(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
    }

    @Test
    void buildValidateAndSaveSource_shouldValidateAndSaveSource() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceName = "book123";

        SourceRequest sourceRequest = SourceRequest.builder()
                .name(sourceName)
                .build();

        CreateSourceRequestParams params = CreateSourceRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceRequest(sourceRequest)
                .build();

        Source result = sourceCreationService.buildValidateAndSaveSource(params);

        assertNotNull(result);
        assertEquals(sourceName, result.getName());
        assertNotNull(result.getId());
    }
}

