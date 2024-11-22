package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileSearchService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CitationFileManagementService {


    private final CitationService citationService;

    private final FileSearchService fileSearchService;

    private final TreeService treeService;

    public CitationFileManagementService(CitationService citationService, FileSearchService fileSearchService, TreeService treeService) {
        this.citationService = citationService;
        this.fileSearchService = fileSearchService;
        this.treeService = treeService;
    }


    @TransactionalInNeo4j
    public void addFileToCitation(UUID treeId, UUID citationId, UUID fileId) {
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);
        File file = fileSearchService.findFileById(fileId);

        if (citation.hasFile(file)) {
            throw new NodeAlreadyInNodeException("File " + fileId + " is already part of the citation " + citationId);
        }
        citation.addFile(file);
        citationService.saveCitation(citation);

        log.info("File {} added successfully to the citation {}", fileId, citationId);
    }

    @TransactionalInNeo4j
    public void removeFileFromCitation(UUID treeId, UUID citationId, UUID fileId) {
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);
        File file = fileSearchService.findFileById(fileId);

        citation.removeFile(file);
        citationService.saveCitation(citation);

        log.info("File {} removed from citation {}", fileId, citationId);
    }
}


