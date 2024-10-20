package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CitationServiceImpl implements CitationService{

    private  final CitationRepository citationRepository;

    public CitationServiceImpl(CitationRepository citationRepository) {
        this.citationRepository = citationRepository;
    }


    public Citation findCitationById(UUID citationId) {
        Optional<Citation> citation = citationRepository.findById(citationId);
        if (citation.isPresent()) {
            log.info("Citation found: {}", citation.get());
            return citation.get();
        } else {
            log.error("No citation found with id: {}", citationId);
            return null;
        }
    }

    public Citation findCitationByIdOrThrowNodeNotFoundException(UUID citationId) {
        Optional<Citation> citation = citationRepository.findById(citationId);
        if (citation.isPresent()) {
            log.info("Citation found: {}", citation.get());
        } else {
            log.error("No citation found with id: {}", citationId);
            throw new NodeNotFoundException("No citation found with id: " + citationId);
        }
        return citation.get();
    }
}
