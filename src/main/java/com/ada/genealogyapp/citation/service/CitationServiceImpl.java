package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CitationServiceImpl implements CitationService {

    private final CitationRepository citationRepository;

    public CitationServiceImpl(CitationRepository citationRepository) {
        this.citationRepository = citationRepository;
    }

    public Citation findCitationById(UUID citationId) {
        return citationRepository.findById(citationId)
                .orElseThrow(() -> new NodeNotFoundException("Citation not found with ID: " + citationId));
    }

    public void ensureCitationExists(UUID citationId) {
        if (!citationRepository.existsById(citationId)) {
            throw new NodeNotFoundException("Citation not found with ID: " + citationId);
        }
    }

    @TransactionalInNeo4j
    public void saveCitation(Citation citation) {
        Citation savedCitation = citationRepository.save(citation);
        log.info("Citation saved successfully: {}", savedCitation);
    }
}
