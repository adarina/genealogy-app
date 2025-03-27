package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.params.AddFileToCitationParams;
import com.ada.genealogyapp.citation.dto.params.RemoveFileFromCitationParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitationFileManagementService {

    private final CitationService citationService;

    @TransactionalInNeo4j
    public void addFileToCitation(AddFileToCitationParams params) {
        citationService.addFileToCitation(params);
    }

    @TransactionalInNeo4j
    public void removeFileFromCitation(RemoveFileFromCitationParams params) {
        citationService.removeFileFromCitation(params);
    }
}


