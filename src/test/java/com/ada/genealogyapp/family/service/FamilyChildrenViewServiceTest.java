package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyChildResponse;
import com.ada.genealogyapp.family.dto.params.GetChildrenParams;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyChildrenViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    FamilyRepository familyRepository;
    @InjectMocks
    FamilyChildrenViewService familyChildrenViewService;

    @Test
    void getChildren_shouldReturnChildrenWhenValidFilter() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";
        Pageable pageable = PageRequest.of(0, 10);

        GetChildrenParams params = GetChildrenParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .pageable(pageable)
                .build();

        Page<FamilyChildResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(familyRepository.findChildren(userId, treeId, familyId, pageable))
                .thenReturn(expectedPage);

        Page<FamilyChildResponse> result = familyChildrenViewService.getChildren(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(treeService).ensureUserAndTreeExist(any(GetChildrenParams.class), eq(expectedPage));
    }

    @Test
    void getChildren_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        Pageable pageable = PageRequest.of(0, 10);

        GetChildrenParams params = GetChildrenParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .pageable(pageable)
                .build();


        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyChildrenViewService.getChildren(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }


    @Test
    void getChildren_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        Pageable pageable = PageRequest.of(0, 10);

        GetChildrenParams params = GetChildrenParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .pageable(pageable)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyChildrenViewService.getChildren(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}
