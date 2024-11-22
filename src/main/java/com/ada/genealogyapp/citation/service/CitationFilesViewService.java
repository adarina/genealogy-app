package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.CitationFilesResponse;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CitationFilesViewService {

    private final CitationRepository citationRepository;
    private final TreeService treeService;
    private final CitationService citationService;

    public CitationFilesViewService(CitationRepository citationRepository, TreeService treeService, CitationService citationService) {
        this.citationRepository = citationRepository;
        this.treeService = treeService;
        this.citationService = citationService;
    }


    public Page<CitationFilesResponse> getCitationFiles(UUID treeId, UUID citationId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        citationService.ensureCitationExists(citationId);
        return citationRepository.findCitationFiles(citationId, pageable);
    }
}
