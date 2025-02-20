package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitationService {

    Citation findCitationById(String citationId);

    void saveCitation(String userId, String treeId, Citation citation);
    void updateCitation(String userId, String treeId, String citationId, Citation citation);

    void saveCitationWithSourceAndEvent(String userId, Citation citation, String sourceId, String eventId);

    void ensureCitationExists(String citationId);

    void deleteCitation(String userId, String treeId, String citationId);

    void addFileToCitation(String userId, String treeId, String citationId, String fileId);

    void addSourceToCitation(String userId, String treeId, String citationId, String sourceId);

    void saveCitationWithSourceAndFiles(String userId, Citation citation, String sourceId, List<String> filesIds);

    void removeFileFromCitation(String userId, String treeId, String citationId, String fileId);

    void removeSourceFromCitation(String userId, String treeId, String citationId, String sourceId);

}
