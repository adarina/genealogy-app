package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.source.dto.params.*;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SourceDataManagerTest {

    @Mock
    SourceRepository sourceRepository;
    @Mock
    QueryResultProcessor processor;
    @InjectMocks
    SourceDataManager sourceDataManager;

    @Test
    void saveSource_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";
        Source source = Source.builder()
                .id(sourceId)
                .name(name)
                .build();

        SaveSourceParams params = SaveSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .source(source)
                .build();

        String result = "SOURCE_CREATED";
        when(sourceRepository.save(userId, treeId, sourceId, source.getName())).thenReturn(result);

        sourceDataManager.saveSource(params);

        verify(sourceRepository).save(userId, treeId, sourceId, source.getName());
        verify(processor).process(result, Map.of(IdType.TREE_ID, treeId, IdType.SOURCE_ID, sourceId));
    }

    @Test
    void saveSource_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";
        Source source = Source.builder()
                .id(sourceId)
                .name(name)
                .build();

        SaveSourceParams params = SaveSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .source(source)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(sourceRepository).save(userId, treeId, sourceId, source.getName());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceDataManager.saveSource(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(sourceRepository).save(userId, treeId, sourceId, source.getName());
    }

    @Test
    void saveSource_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";
        Source source = Source.builder()
                .id(sourceId)
                .name(name)
                .build();

        SaveSourceParams params = SaveSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .source(source)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(sourceRepository).save(userId, treeId, sourceId, source.getName());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceDataManager.saveSource(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(sourceRepository).save(userId, treeId, sourceId, source.getName());
    }

    @Test
    void updateSource_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";
        Source source = Source.builder()
                .id(sourceId)
                .name(name)
                .build();

        UpdateSourceParams params = UpdateSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .source(source)
                .build();

        String result = "SOURCE_UPDATED";
        when(sourceRepository.update(userId, treeId, sourceId, source.getName())).thenReturn(result);

        sourceDataManager.updateSource(params);

        verify(sourceRepository).update(userId, treeId, sourceId, source.getName());
        verify(processor).process(result, Map.of(IdType.SOURCE_ID, sourceId));
    }

    @Test
    void updateSource_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";
        Source source = Source.builder()
                .id(sourceId)
                .name(name)
                .build();

        UpdateSourceParams params = UpdateSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .source(source)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(sourceRepository).update(userId, treeId, sourceId, source.getName());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceDataManager.updateSource(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(sourceRepository).update(userId, treeId, sourceId, source.getName());
    }

    @Test
    void updateSource_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";
        String name = "book123";
        Source source = Source.builder()
                .id(sourceId)
                .name(name)
                .build();

        UpdateSourceParams params = UpdateSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .source(source)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(sourceRepository).update(userId, treeId, sourceId, source.getName());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceDataManager.updateSource(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(sourceRepository).update(userId, treeId, sourceId, source.getName());
    }

    @Test
    void deleteSource_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        DeleteSourceParams params = DeleteSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        String result = "SOURCE_DELETED";
        when(sourceRepository.delete(userId, treeId, sourceId)).thenReturn(result);

        sourceDataManager.deleteSource(params);

        verify(sourceRepository).delete(userId, treeId, sourceId);
        verify(processor).process(result, Map.of(IdType.SOURCE_ID, sourceId));
    }

    @Test
    void deleteSource_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        DeleteSourceParams params = DeleteSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(sourceRepository).delete(userId, treeId, sourceId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceDataManager.deleteSource(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(sourceRepository).delete(userId, treeId, sourceId);
    }

    @Test
    void deleteSource_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String sourceId = "source123";

        DeleteSourceParams params = DeleteSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(sourceRepository).delete(userId, treeId, sourceId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> sourceDataManager.deleteSource(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(sourceRepository).delete(userId, treeId, sourceId);
    }
}
