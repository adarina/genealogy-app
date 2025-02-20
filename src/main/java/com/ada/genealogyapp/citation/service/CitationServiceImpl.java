package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CitationServiceImpl implements CitationService {

    private final CitationRepository citationRepository;

    private final QueryResultProcessor queryResultProcessor;

    public CitationServiceImpl(CitationRepository citationRepository, QueryResultProcessor queryResultProcessor) {
        this.citationRepository = citationRepository;
        this.queryResultProcessor = queryResultProcessor;
    }

    public Citation findCitationById(String citationId) {
        return citationRepository.findById(citationId)
                .orElseThrow(() -> new NodeNotFoundException("Citation not found with ID: " + citationId));
    }

    public void ensureCitationExists(String citationId) {
        if (!citationRepository.existsById(citationId)) {
            throw new NodeNotFoundException("Citation not found with ID: " + citationId);
        }
    }

    @TransactionalInNeo4j
    public void saveCitation(String userId, String treeId, @NonNull Citation citation) {
        String result = citationRepository.save(userId, treeId, citation.getId(), citation.getPage(), citation.getDate());
        queryResultProcessor.process(result, Map.of("treeId", treeId, "citationId", citation.getId()));

    }

    @TransactionalInNeo4j
    public void deleteCitation(String userId, String treeId, String citationId) {
        String result = citationRepository.delete(userId, treeId, citationId);
        queryResultProcessor.process(result, Map.of("citationId", citationId));
    }

    @TransactionalInNeo4j
    public void updateCitation(String userId, String treeId, String citationId, Citation citation) {
        String result = citationRepository.update(userId, treeId, citationId, citation.getPage(), citation.getDate());
        queryResultProcessor.process(result, Map.of("citationId", citationId));
    }

    @TransactionalInNeo4j
    public void saveCitationWithSourceAndEvent(String userId, Citation citation, String sourceId, String eventId) {
        String result = citationRepository.save(userId, citation.getTree().getId(), citation.getId(), citation.getPage(), citation.getDate(), sourceId, eventId);
        queryResultProcessor.process(result, Map.of("treeId", citation.getTree().getId(), "citationId", citation.getId()));
    }

    @TransactionalInNeo4j
    public void saveCitationWithSourceAndFiles(String userId, Citation citation, String sourceId, List<String> filesIds) {
        String result = citationRepository.save(userId, citation.getId(), citation.getPage(), citation.getDate(), citation.getTree().getId(), sourceId, filesIds);
        queryResultProcessor.process(result, Map.of("treeId", citation.getTree().getId(), "citationId", citation.getId()));

    }

    @TransactionalInNeo4j
    public void addFileToCitation(String userId, String treeId, String citationId, String fileId) {
        String result = citationRepository.addFile(userId, treeId, citationId, fileId);
        queryResultProcessor.process(result, Map.of("citationId", citationId, "fileId", fileId));
    }

    @TransactionalInNeo4j
    public void removeFileFromCitation(String userId, String treeId, String citationId, String fileId) {
        String result = citationRepository.removeFile(userId, treeId, citationId, fileId);
        queryResultProcessor.process(result, Map.of("citationId", citationId, "fileId", fileId));
    }

    @TransactionalInNeo4j
    public void addSourceToCitation(String userId, String treeId, String citationId, String sourceId) {
        String result = citationRepository.addSource(userId, treeId, citationId, sourceId);
        queryResultProcessor.process(result, Map.of("citationId", citationId, "sourceId", sourceId));
    }

    @TransactionalInNeo4j
    public void removeSourceFromCitation(String userId, String treeId, String citationId, String sourceId) {
        String result = citationRepository.removeSource(userId, treeId, citationId, sourceId);
        queryResultProcessor.process(result, Map.of("citationId", citationId, "sourceId", sourceId));
    }
}
