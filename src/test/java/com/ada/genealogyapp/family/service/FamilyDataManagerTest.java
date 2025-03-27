package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.family.dto.params.DeleteFamilyParams;
import com.ada.genealogyapp.family.dto.params.SaveFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyParams;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class FamilyDataManagerTest {

    @Mock
    FamilyRepository familyRepository;
    @Mock
    QueryResultProcessor processor;
    @InjectMocks
    FamilyDataManager familyDataManager;

    @Test
    void saveFamily_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        StatusType status = StatusType.MARRIED;
        Family family = Family.builder()
               
                .status(status)
                .build();

        SaveFamilyParams params = SaveFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(family.getId())
                .family(family)
                .build();

        String result = "FAMILY_CREATED";
        when(familyRepository.save(userId, treeId, family.getId(), family.getStatus().name())).thenReturn(result);

        familyDataManager.saveFamily(params);

        verify(familyRepository).save(userId, treeId, family.getId(), family.getStatus().name());
        verify(processor).process(result, Map.of(IdType.TREE_ID, treeId, IdType.FAMILY_ID, family.getId()));
    }

    @Test
    void saveFamily_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        StatusType status = StatusType.MARRIED;

        Family family = Family.builder()

                .status(status)
                .build();

        SaveFamilyParams params = SaveFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(family.getId())
                .family(family)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(familyRepository).save(userId, treeId, family.getId(), family.getStatus().name());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyDataManager.saveFamily(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(familyRepository).save(userId, treeId, family.getId(), family.getStatus().name());
    }

    @Test
    void saveFamily_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
      
        StatusType status = StatusType.MARRIED;

        Family family = Family.builder()

                .status(status)
                .build();

        SaveFamilyParams params = SaveFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(family.getId())
                .family(family)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(familyRepository).save(userId, treeId, family.getId(), family.getStatus().name());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyDataManager.saveFamily(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(familyRepository).save(userId, treeId, family.getId(), family.getStatus().name());
    }

    @Test
    void updateFamily_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
       
        StatusType status = StatusType.MARRIED;

        Family family = Family.builder()

                .status(status)
                .build();

        UpdateFamilyParams params = UpdateFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(family.getId())
                .family(family)
                .build();

        String result = "FAMILY_UPDATED";
        when(familyRepository.update(userId, treeId, family.getId(), family.getStatus().name())).thenReturn(result);

        familyDataManager.updateFamily(params);

        verify(familyRepository).update(userId, treeId, family.getId(), family.getStatus().name());
        verify(processor).process(result, Map.of(IdType.FAMILY_ID, family.getId()));
    }

    @Test
    void updateFamily_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        
        StatusType status = StatusType.MARRIED;

        Family family = Family.builder()

                .status(status)
                .build();

        UpdateFamilyParams params = UpdateFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(family.getId())
                .family(family)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(familyRepository).update(userId, treeId, family.getId(), family.getStatus().name());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyDataManager.updateFamily(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(familyRepository).update(userId, treeId, family.getId(), family.getStatus().name());
    }

    @Test
    void updateFamily_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        
        StatusType status = StatusType.MARRIED;

        Family family = Family.builder()

                .status(status)
                .build();

        UpdateFamilyParams params = UpdateFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(family.getId())
                .family(family)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(familyRepository).update(userId, treeId, family.getId(), family.getStatus().name());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyDataManager.updateFamily(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(familyRepository).update(userId, treeId, family.getId(), family.getStatus().name());
    }

    @Test
    void deleteFamily_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
       String familyId = "family123";

        DeleteFamilyParams params = DeleteFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        String result = "FAMILY_DELETED";
        when(familyRepository.delete(userId, treeId, familyId)).thenReturn(result);

        familyDataManager.deleteFamily(params);

        verify(familyRepository).delete(userId, treeId, familyId);
        verify(processor).process(result, Map.of(IdType.FAMILY_ID, familyId));
    }

    @Test
    void deleteFamily_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        DeleteFamilyParams params = DeleteFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(familyRepository).delete(userId, treeId, familyId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyDataManager.deleteFamily(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(familyRepository).delete(userId, treeId, familyId);
    }

    @Test
    void deleteFamily_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String familyId = "family123";

        DeleteFamilyParams params = DeleteFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(familyRepository).delete(userId, treeId, familyId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> familyDataManager.deleteFamily(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(familyRepository).delete(userId, treeId, familyId);
    }
}

