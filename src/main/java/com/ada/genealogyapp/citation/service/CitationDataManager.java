package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.citation.dto.params.*;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitationDataManager implements CitationService {

    private final CitationRepository citationRepository;

    private final QueryResultProcessor processor;

    public void saveCitation(SaveCitationParams params) {
        String result = citationRepository.save(params.getUserId(), params.getTreeId(), params.getCitationId(), params.getCitation().getPage(), params.getCitation().getDate());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.CITATION_ID, params.getCitationId()));
    }

    public void deleteCitation(DeleteCitationParams params) {
        String result = citationRepository.delete(params.getUserId(), params.getTreeId(), params.getCitationId());
        processor.process(result, Map.of(IdType.CITATION_ID, params.getCitationId()));
    }

    public void updateCitation(UpdateCitationParams params) {
        String result = citationRepository.update(params.getUserId(), params.getTreeId(), params.getCitationId(), params.getCitation().getPage(), params.getCitation().getDate());
        processor.process(result, Map.of(IdType.CITATION_ID, params.getCitationId()));
    }

    public void saveCitationWithSourceAndEvent(SaveCitationWithSourceAndEventParams params) {
        String result = citationRepository.save(params.getUserId(), params.getTreeId(), params.getCitation().getId(), params.getCitation().getPage(), params.getCitation().getDate(), params.getSourceId(), params.getEventId());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.CITATION_ID, params.getCitationId()));
    }

    public void saveCitationWithSourceAndFiles(SaveCitationWithSourceAndFilesParams params) {
        String result = citationRepository.save(params.getUserId(), params.getTreeId(), params.getCitation().getPage(), params.getCitation().getDate(), params.getCitationId(), params.getSourceId(), params.getFilesIds());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.CITATION_ID, params.getCitationId()));
    }

    public void addFileToCitation(AddFileToCitationParams params) {
        String result = citationRepository.addFile(params.getUserId(), params.getTreeId(), params.getCitationId(), params.getFileId());
        processor.process(result, Map.of(IdType.CITATION_ID, params.getCitationId(), IdType.FILE_ID, params.getFileId()));
    }

    public void removeFileFromCitation(RemoveFileFromCitationParams params) {
        String result = citationRepository.removeFile(params.getUserId(), params.getTreeId(), params.getCitationId(), params.getFileId());
        processor.process(result, Map.of(IdType.CITATION_ID, params.getCitationId(), IdType.FILE_ID, params.getFileId()));
    }

    public void addSourceToCitation(AddSourceToCitationParams params) {
        String result = citationRepository.addSource(params.getUserId(), params.getTreeId(), params.getCitationId(), params.getSourceId());
        processor.process(result, Map.of(IdType.CITATION_ID, params.getCitationId(), IdType.SOURCE_ID, params.getSourceId()));
    }

    public void removeSourceFromCitation(RemoveSourceFromCitationParams params) {
        String result = citationRepository.removeSource(params.getUserId(), params.getTreeId(), params.getCitationId(), params.getSourceId());
        processor.process(result, Map.of(IdType.CITATION_ID, params.getCitationId(), IdType.SOURCE_ID, params.getSourceId()));
    }

    public void saveCitations(String userId, String treeId, List<Map<String, Object>> citationsData) {
        citationRepository.saveCitations(userId, treeId, citationsData);
    }

    @TransactionalInNeo4j
    public void addFilesToEvents(String userId, String treeId, List<Map<String, String>> filesToAdd) {
        citationRepository.addFilesToEvents(userId, treeId, filesToAdd);
    }

    @TransactionalInNeo4j
    public void addSourcesToEvents(String userId, String treeId, List<Map<String, String>> sourcesToAdd) {
        citationRepository.addSourcesToEvents(userId, treeId, sourcesToAdd);
    }

    @TransactionalInNeo4j
    public void addFilesAndSourcesToEvents(String userId, String treeId, List<Map<String, String>> filesToAdd, List<Map<String, String>> sourcesToAdd) {
        citationRepository.addFilesAndSourcesToEvents(userId, treeId, filesToAdd, sourcesToAdd);
    }

}
