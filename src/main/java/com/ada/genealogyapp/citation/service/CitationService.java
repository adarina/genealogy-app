package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;

import java.util.UUID;

public interface CitationService {

    Citation findCitationByIdOrThrowNodeNotFoundException(UUID citationId);
}
