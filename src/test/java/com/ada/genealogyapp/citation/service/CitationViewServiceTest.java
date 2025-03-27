package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.citation.dto.CitationFilterRequest;
import com.ada.genealogyapp.citation.dto.params.GetCitationParams;
import com.ada.genealogyapp.citation.dto.params.GetCitationsParams;
import com.ada.genealogyapp.citation.repository.CitationRepository;
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
class CitationViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    CitationRepository citationRepository;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    CitationViewService citationViewService;

    @Test
    void getCitations_shouldReturnCitationsWhenValidFilter() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"page\":\"page123\",\"name\":\"book123\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetCitationsParams params = GetCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        CitationFilterRequest filterRequest = CitationFilterRequest.builder()
                .page("page123")
                .name("book123")
                .build();

        Page<CitationSourceResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(objectMapper.readValue(filter, CitationFilterRequest.class)).thenReturn(filterRequest);
        when(citationRepository.find(userId, treeId, "page123", "book123", pageable))
                .thenReturn(expectedPage);

        Page<CitationSourceResponse> result = citationViewService.getCitations(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(objectMapper).readValue(eq(filter), eq(CitationFilterRequest.class));
        verify(treeService).ensureUserAndTreeExist(any(GetCitationsParams.class), eq(expectedPage));
    }

    @Test
    void getCitations_shouldThrowExceptionWhenUserDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"page\":\"page123\",\"name\":\"book123\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetCitationsParams params = GetCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        CitationFilterRequest filterRequest = CitationFilterRequest.builder()
                .page("page123")
                .name("book123")
                .build();

        when(objectMapper.readValue(filter, CitationFilterRequest.class)).thenReturn(filterRequest);
        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> citationViewService.getCitations(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getCitations_shouldThrowExceptionWhenTreeDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"page\":\"page123\",\"name\":\"book123\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetCitationsParams params = GetCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        CitationFilterRequest filterRequest = CitationFilterRequest.builder()
                .page("page123")
                .name("book123")
                .build();

        when(objectMapper.readValue(filter, CitationFilterRequest.class)).thenReturn(filterRequest);
        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> citationViewService.getCitations(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getCitation_shouldReturnCitationWhenExist() {
        String userId = "user123";
        String treeId = "tree123";
        String citationId = "citation123";

        GetCitationParams params = GetCitationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .build();

        CitationSourceResponse expectedCitation = CitationSourceResponse.builder()
                .id(citationId)
                .build();

        when(citationRepository.find(userId, treeId, citationId)).thenReturn(expectedCitation);

        CitationSourceResponse result = citationViewService.getCitation(params);

        assertNotNull(result);
        assertEquals(expectedCitation, result);
        verify(treeService).ensureUserAndTreeExist(any(GetCitationParams.class), eq(expectedCitation));
    }


    @Test
    void getCitation_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String citationId = "citation123";

        GetCitationParams params = GetCitationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> citationViewService.getCitation(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getCitation_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String citationId = "citation123";

        GetCitationParams params = GetCitationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> citationViewService.getCitation(params));

        assertEquals("User not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}