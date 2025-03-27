package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.CitationFilterRequest;
import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.citation.dto.params.GetCitationParams;
import com.ada.genealogyapp.citation.dto.params.GetCitationsParams;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CitationViewService {

    private final TreeService treeService;

    private final ObjectMapper objectMapper;

    private final CitationRepository citationRepository;


    public Page<CitationSourceResponse> getCitations(GetCitationsParams params) throws JsonProcessingException {
        CitationFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), CitationFilterRequest.class);
        Page<CitationSourceResponse> page = citationRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getPage(), filterRequest.getName(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public CitationSourceResponse getCitation(GetCitationParams params) {
        CitationSourceResponse citationResponse = citationRepository.find(params.getUserId(), params.getTreeId(), params.getCitationId());
        treeService.ensureUserAndTreeExist(params, citationResponse);
        return citationResponse;
    }
}