package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.*;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FamilyViewServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    FamilyService familyService;

    @Mock
    FamilyRepository familyRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    FamilyViewService familyViewService;

    String treeId;
    String familyId;
    Pageable pageable;
    String filter;

    @BeforeEach
    void setUp() {

        treeId = String.valueOf(UUID.randomUUID());
        familyId = String.valueOf(UUID.randomUUID());
        filter = "{\"motherName\":\"Elizabeth Black\",\"fatherName\":\"John Smith\",\"status\":\"MARRIED\"}";
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getFamilies_shouldReturnFamiliesWhenValidFilter() throws JsonProcessingException {

        FamilyFilterRequest filterRequest = new FamilyFilterRequest("Elizabeth Black", "John Smith", "MARRIED");
        Page<FamilyResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(objectMapper.readValue(filter, FamilyFilterRequest.class)).thenReturn(filterRequest);
        when(familyRepository.findByTreeIdAndFilteredParentNamesAndStatus(
                treeId, "Elizabeth Black", "John Smith", "MARRIED", pageable
        )).thenReturn(expectedPage);

        Page<FamilyResponse> result = familyViewService.getFamilies(treeId, filter, pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(treeService).ensureTreeExists(treeId);
        verify(objectMapper).readValue(filter, FamilyFilterRequest.class);
        verify(familyRepository).findByTreeIdAndFilteredParentNamesAndStatus(
                treeId, "Elizabeth Black", "John Smith", "MARRIED", pageable);
    }

    @Test
    void getFamilies_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            familyViewService.getFamilies(treeId, filter, pageable);
        });

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(objectMapper, familyRepository);
    }

    @Test
    void getFamily_shouldReturnFamilyWhenExists() {

        String fatherId = String.valueOf(UUID.randomUUID());

        FamilyResponse expectedResponse = new FamilyResponse();
        expectedResponse.setId(familyId);
        expectedResponse.setStatus(StatusType.MARRIED);
        expectedResponse.setFatherId(fatherId);
        expectedResponse.setFatherName("John Smith");

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(familyService).ensureFamilyExists(familyId);
        when(familyRepository.findByTreeIdAndFamilyId(treeId, familyId)).thenReturn(Optional.of(expectedResponse));

        FamilyResponse result = familyViewService.getFamily(treeId, familyId);

        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getFatherId(), result.getFatherId());
        assertEquals(expectedResponse.getFatherName(), result.getFatherName());
        assertEquals(expectedResponse.getStatus(), result.getStatus());
        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).ensureFamilyExists(familyId);
        verify(familyRepository).findByTreeIdAndFamilyId(treeId, familyId);
    }

    @Test
    void getFamily_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            familyViewService.getFamily(treeId, familyId);
        });
        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(familyService, familyRepository);
    }

    @Test
    void getFamily_shouldThrowExceptionWhenFamilyDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(familyService).ensureFamilyExists(familyId);
        when(familyRepository.findByTreeIdAndFamilyId(treeId, familyId))
                .thenReturn(Optional.empty());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            familyViewService.getFamily(treeId, familyId);
        });

        assertEquals("Family " + familyId + " not found for tree " + treeId, exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(familyService).ensureFamilyExists(familyId);
        verify(familyRepository).findByTreeIdAndFamilyId(treeId, familyId);
    }
}
