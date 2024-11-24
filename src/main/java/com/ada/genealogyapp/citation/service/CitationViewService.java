package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.CitationFilterRequest;
import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CitationViewService {

    private final TreeService treeService;
    private final ObjectMapper objectMapper;
    private final CitationService citationService;
    private final CitationRepository citationRepository;

    public CitationViewService(TreeService treeService, ObjectMapper objectMapper, CitationService citationService, CitationRepository citationRepository) {
        this.treeService = treeService;
        this.objectMapper = objectMapper;
        this.citationService = citationService;
        this.citationRepository = citationRepository;
    }


    public Page<CitationSourceResponse> getCitations(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        treeService.ensureTreeExists(treeId);
        CitationFilterRequest filterRequest = objectMapper.readValue(filter, CitationFilterRequest.class);
        return citationRepository.findByTreeIdAndFilteredPage(
                treeId,
                Optional.ofNullable(filterRequest.getPage()).orElse(""),
                pageable
        );
    }

    public CitationSourceResponse getCitation(UUID treeId, UUID citationId) {
        treeService.ensureTreeExists(treeId);
        citationService.ensureCitationExists(citationId);
        return citationRepository.findByTreeIdAndCitationId(treeId, citationId)
                .orElseThrow(() -> new NodeNotFoundException("Citation " + citationId.toString() + " not found for tree " + treeId.toString()));
    }
}
