package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.source.dto.SourceFilterRequest;
import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.dto.params.GetSourceParams;
import com.ada.genealogyapp.source.dto.params.GetSourcesParams;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SourceViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    SourceRepository sourceRepository;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    SourceViewService sourceViewService;

    @Test
    void getSources_shouldReturnSourcesWhenValidFilter() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"name\":\"book123\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetSourcesParams params = GetSourcesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        SourceFilterRequest filterRequest = SourceFilterRequest.builder()
                .name("book123")
                .build();

        Page<SourceResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(objectMapper.readValue(filter, SourceFilterRequest.class)).thenReturn(filterRequest);
        when(sourceRepository.find(userId, treeId, "book123", pageable))
                .thenReturn(expectedPage);

        Page<SourceResponse> result = sourceViewService.getSources(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(objectMapper).readValue(eq(filter), eq(SourceFilterRequest.class));
        verify(treeService).ensureUserAndTreeExist(any(GetSourcesParams.class), eq(expectedPage));
    }

    @Test
    void getSources_shouldThrowExceptionWhenUserDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"name\":\"book1234\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetSourcesParams params = GetSourcesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        SourceFilterRequest filterRequest = SourceFilterRequest.builder()
                .name("book123")
                .build();

        when(objectMapper.readValue(filter, SourceFilterRequest.class)).thenReturn(filterRequest);
        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceViewService.getSources(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getSources_shouldThrowExceptionWhenTreeDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"name\":\"book1234\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetSourcesParams params = GetSourcesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        SourceFilterRequest filterRequest = SourceFilterRequest.builder()
                .name("book123")
                .build();

        when(objectMapper.readValue(filter, SourceFilterRequest.class)).thenReturn(filterRequest);
        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceViewService.getSources(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getSource_shouldReturnSourceWhenExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        GetSourceParams params = GetSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        SourceResponse expectedSource = SourceResponse.builder()
                .id(sourceId)
                .build();

        when(sourceRepository.find(userId, treeId, sourceId)).thenReturn(expectedSource);

        SourceResponse result = sourceViewService.getSource(params);

        assertNotNull(result);
        assertEquals(expectedSource, result);
        verify(treeService).ensureUserAndTreeExist(any(GetSourceParams.class), eq(expectedSource));
    }


    @Test
    void getSource_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        GetSourceParams params = GetSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceViewService.getSource(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getSource_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        GetSourceParams params = GetSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceViewService.getSource(params));

        assertEquals("User not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}