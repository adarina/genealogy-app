package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public Source findSourceById(UUID sourceId) {
        return sourceRepository.findById(sourceId)
                .orElseThrow(() -> new NodeNotFoundException("Source not found with ID: " + sourceId));
    }

    public void ensureSourceExists(UUID sourceId) {
        if (!sourceRepository.existsById(sourceId)) {
            throw new NodeNotFoundException("Source not found with ID: " + sourceId);
        }
    }

    @TransactionalInNeo4j
    public void saveSource(Source source) {
        Source savedSource = sourceRepository.save(source);
        log.info("Source saved successfully: {}", savedSource);
    }
}
