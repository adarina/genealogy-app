package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    private final QueryResultProcessor queryResultProcessor;

    public SourceServiceImpl(SourceRepository sourceRepository, QueryResultProcessor queryResultProcessor) {
        this.sourceRepository = sourceRepository;
        this.queryResultProcessor = queryResultProcessor;
    }

    public void ensureSourceExists(String sourceId) {
        if (!sourceRepository.existsById(sourceId)) {
            throw new NodeNotFoundException("Source not found with ID: " + sourceId);
        }
    }

    @TransactionalInNeo4j
    public void saveSource(String treeId, @NonNull Source source) {
        String result = sourceRepository.save(treeId, source.getId(), source.getName());
        queryResultProcessor.process(result, Map.of("treeId", treeId, "sourceId", source.getId()));
    }

    @TransactionalInNeo4j
    public void updateSource(String treeId, String sourceId, Source source) {
        String result = sourceRepository.update(treeId, sourceId, source.getName());
        queryResultProcessor.process(result, Map.of("sourceId", sourceId));
    }
}