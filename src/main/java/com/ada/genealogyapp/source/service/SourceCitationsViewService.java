package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.dto.SourceCitationResponse;
import com.ada.genealogyapp.source.dto.params.GetSourceCitationParams;
import com.ada.genealogyapp.source.dto.params.GetSourceCitationsParams;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SourceCitationsViewService {

    private final SourceRepository sourceRepository;

    private final TreeService treeService;


    public Page<SourceCitationResponse> getSourceCitations(GetSourceCitationsParams params) {
        Page<SourceCitationResponse> page = sourceRepository.findSourceCitations(params.getUserId(), params.getTreeId(), params.getSourceId(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public SourceCitationResponse getSourceCitation(GetSourceCitationParams params) {
        SourceCitationResponse sourceCitationResponse = sourceRepository.findSourceCitation(params.getUserId(), params.getTreeId(), params.getSourceId(), params.getCitationId());
        treeService.ensureUserAndTreeExist(params, sourceCitationResponse);
        return sourceCitationResponse;
    }
}

