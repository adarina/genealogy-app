package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public Source findSourceById(UUID sourceId) {
        Optional<Source> source = sourceRepository.findById(sourceId);
        if (source.isPresent()) {
            log.info("Citation found: {}", source.get());
            return source.get();
        } else {
            log.error("No citation found with id: {}", sourceId);
            return null;
        }
    }
}
