package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.params.*;

import java.util.List;
import java.util.Map;

public interface CitationService {

    void saveCitation(SaveCitationParams params);

    void updateCitation(UpdateCitationParams params);

    void saveCitationWithSourceAndEvent(SaveCitationWithSourceAndEventParams params);

    void deleteCitation(DeleteCitationParams params);

    void addFileToCitation(AddFileToCitationParams params);

    void addSourceToCitation(AddSourceToCitationParams params);

    void saveCitationWithSourceAndFiles(SaveCitationWithSourceAndFilesParams params);

    void removeFileFromCitation(RemoveFileFromCitationParams params);

    void removeSourceFromCitation(RemoveSourceFromCitationParams params);

    void updateCitationWithSource(UpdateCitationWithSourceParams build);
}

