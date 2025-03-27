package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.citation.dto.params.GetCitationFilesParams;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.tree.service.TreeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitationFilesViewServiceTest {

    @Value("${file.upload.base-url}")
    String baseUrl;
    @Mock
    TreeService treeService;
    @Mock
    CitationRepository citationRepository;
    @InjectMocks
    CitationFilesViewService citationFilesViewService;

    @Test
    void getCitationFiles_shouldReturnCitationFilesWhenValidFilter() {
        String userId = "user123";
        String treeId = "tree123";
        String citationId = "citationId";
        Pageable pageable = PageRequest.of(0, 10);

        GetCitationFilesParams params = GetCitationFilesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .pageable(pageable)
                .build();

        Page<FileResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(citationRepository.findFiles(userId, treeId, citationId, baseUrl, pageable))
                .thenReturn(expectedPage);

        Page<FileResponse> result = citationFilesViewService.getCitationFiles(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(treeService).ensureUserAndTreeExist(any(GetCitationFilesParams.class), eq(expectedPage));
    }

    @Test
    void getCitationFiles_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String citationId = "citationId";
        Pageable pageable = PageRequest.of(0, 10);

        GetCitationFilesParams params = GetCitationFilesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .pageable(pageable)
                .build();


        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> citationFilesViewService.getCitationFiles(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getCitationFiles_shouldThrowExceptionWhenTreeDoesNotExist()  {
        String userId = "user123";
        String treeId = "tree123";
        String citationId = "citationId";
        Pageable pageable = PageRequest.of(0, 10);

        GetCitationFilesParams params = GetCitationFilesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .pageable(pageable)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> citationFilesViewService.getCitationFiles(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}