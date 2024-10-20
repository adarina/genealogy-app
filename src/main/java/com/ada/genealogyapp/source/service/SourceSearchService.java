package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SourceSearchService {

    SourceRepository sourceRepository;

    public Source findSourceByIdOrThrowNodeNotFoundException(UUID sourceId) {
        Optional<Source> source = sourceRepository.findById(sourceId);
        if (source.isPresent()) {
            log.info("Source found: {}", source.get());
        } else {
            log.error("No source found with id: {}", sourceId);
            throw new NodeNotFoundException("No source found with id: " + sourceId);
        }
        return source.get();
    }
}
