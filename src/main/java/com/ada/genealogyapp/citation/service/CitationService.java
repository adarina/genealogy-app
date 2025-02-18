package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitationService {

    Citation findCitationById(String citationId);

    void saveCitation(String treeId, Citation citation);
    void updateCitation(String treeId, String citationId, Citation citation);

    void saveCitationWithSourceAndEvent(Citation citation, String sourceId, String eventId);

    void ensureCitationExists(String citationId);

    void deleteCitation(String treeId, String citationId);

    void addFileToCitation(String treeId, String citationId, String fileId);

    void addSourceToCitation(String treeId, String citationId, String sourceId);

    void saveCitationWithSourceAndFiles(Citation citation, String sourceId, List<String> filesIds);

    void removeFileFromCitation(String treeId, String citationId, String fileId);

    void removeSourceFromCitation(String treeId, String citationId, String sourceId);

}
