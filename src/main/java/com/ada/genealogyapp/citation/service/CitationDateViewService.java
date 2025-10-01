package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.CitationResponse;
import com.ada.genealogyapp.citation.dto.params.GetCitationParams;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ada.genealogyapp.date.service.DateExtractor.parseDate;


@Service
@Slf4j
@RequiredArgsConstructor
public class CitationDateViewService {

    private final CitationRepository citationRepository;

    private final TreeService treeService;

    public Date getCitationDate(GetCitationParams params) {
        CitationResponse citationResponse = citationRepository.find(params.getUserId(), params.getTreeId(), params.getCitationId());
        treeService.ensureUserAndTreeExist(params, citationResponse);
        return parseDate(citationResponse.getDate());
    }
}
