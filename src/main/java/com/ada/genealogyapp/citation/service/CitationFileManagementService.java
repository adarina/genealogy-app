package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileSearchService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    // TODO file nie jest fileid tylko plik
    @TransactionalInNeo4j
    public void addFileToCitation(String userId, String treeId, String citationId, String fileId) {
//        treeService.ensureTreeExists(treeId);
//        Citation citation = citationService.findCitationById(citationId);
//        File file = fileSearchService.findFileById(fileId);
//
//        if (citation.hasFile(file)) {
//            throw new NodeAlreadyInNodeException("File " + fileId + " is already part of the citation " + citationId);
//        }
//        citation.addFile(file);
//        citationService.saveCitation(citation);

//        log.info("File {} added successfully to the citation {}", fileId, citationId);
        citationService.addFileToCitation(userId, treeId, citationId, fileId);
    }

    @TransactionalInNeo4j
    public void removeFileFromCitation(String userId, String treeId, String citationId, String fileId) {
        citationService.removeFileFromCitation(userId, treeId, citationId, fileId);
    }
}


