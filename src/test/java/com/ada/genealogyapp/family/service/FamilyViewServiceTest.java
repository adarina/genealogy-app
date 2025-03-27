package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.family.dto.*;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.params.GetFamiliesParams;
import com.ada.genealogyapp.family.dto.params.GetFamilyParams;


import com.ada.genealogyapp.family.repository.FamilyRepository;
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
class FamilyViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    FamilyRepository familyRepository;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    FamilyViewService familyViewService;

    @Test
    void getFamilies_shouldReturnFamiliesWhenValidFilter() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"motherName\":\"Elizabeth Black\",\"fatherName\":\"John Smith\",\"status\":\"MARRIED\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetFamiliesParams params = GetFamiliesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        FamilyFilterRequest filterRequest = FamilyFilterRequest.builder()
                .motherName("Elizabeth Black")
                .fatherName("John Smith")
                .status("MARRIED")
                .build();

        Page<FamiliesResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(objectMapper.readValue(filter, FamilyFilterRequest.class)).thenReturn(filterRequest);
        when(familyRepository.find(userId, treeId, "John Smith", "Elizabeth Black", "MARRIED", pageable))
                .thenReturn(expectedPage);

        Page<FamiliesResponse> result = familyViewService.getFamilies(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(objectMapper).readValue(eq(filter), eq(FamilyFilterRequest.class));
        verify(treeService).ensureUserAndTreeExist(any(GetFamiliesParams.class), eq(expectedPage));
    }

    @Test
    void getFamilies_shouldThrowExceptionWhenUserDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"motherName\":\"Elizabeth Black\",\"fatherName\":\"John Smith\",\"status\":\"MARRIED\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetFamiliesParams params = GetFamiliesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        FamilyFilterRequest filterRequest = FamilyFilterRequest.builder()
                .motherName("Elizabeth Black")
                .fatherName("John Smith")
                .status("MARRIED")
                .build();

        when(objectMapper.readValue(filter, FamilyFilterRequest.class)).thenReturn(filterRequest);
        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyViewService.getFamilies(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }


    @Test
    void getFamilies_shouldThrowExceptionWhenTreeDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"motherName\":\"Elizabeth Black\",\"fatherName\":\"John Smith\",\"status\":\"MARRIED\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetFamiliesParams params = GetFamiliesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        FamilyFilterRequest filterRequest = FamilyFilterRequest.builder()
                .motherName("Elizabeth Black")
                .fatherName("John Smith")
                .status("MARRIED")
                .build();

        when(objectMapper.readValue(filter, FamilyFilterRequest.class)).thenReturn(filterRequest);

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyViewService.getFamilies(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getFamily_shouldReturnFamilyWhenExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        GetFamilyParams params = GetFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        FamilyResponse expectedFamily = FamilyResponse.builder()
                .id(familyId)
                .build();

        when(familyRepository.find(userId, treeId, familyId))
                .thenReturn(expectedFamily);

        FamilyResponse result = familyViewService.getFamily(params);

        assertNotNull(result);
        assertEquals(expectedFamily, result);
        verify(treeService).ensureUserAndTreeExist(any(GetFamilyParams.class), eq(expectedFamily));
    }

    @Test
    void getFamily_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        GetFamilyParams params = GetFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyViewService.getFamily(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getFamily_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        GetFamilyParams params = GetFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyViewService.getFamily(params));

        assertEquals("User not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}
