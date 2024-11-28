package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;

public interface CitationService {

    Citation findCitationById(String citationId);

    void saveCitation(Citation citation);

    void ensureCitationExists(String citationId);

    void deleteCitation(Citation citation);
}
