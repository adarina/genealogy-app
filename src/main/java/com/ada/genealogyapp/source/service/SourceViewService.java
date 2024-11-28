package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.source.dto.SourceFilterRequest;
import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.dto.SourcesResponse;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SourceViewService {

    private final TreeService treeService;
    private final ObjectMapper objectMapper;
    private final SourceService sourceService;
    private final SourceRepository sourceRepository;

    public SourceViewService(TreeService treeService, ObjectMapper objectMapper, SourceService sourceService, SourceRepository sourceRepository) {
        this.treeService = treeService;
        this.objectMapper = objectMapper;
        this.sourceService = sourceService;
        this.sourceRepository = sourceRepository;
    }

    public Page<SourcesResponse> getSources(String treeId, String filter, Pageable pageable) throws JsonProcessingException {
        treeService.ensureTreeExists(treeId);
        SourceFilterRequest filterRequest = objectMapper.readValue(filter, SourceFilterRequest.class);
        return sourceRepository.findByTreeIdAndFilteredName(
                treeId,
                Optional.ofNullable(filterRequest.getName()).orElse(""),
                pageable
        );
    }

    public SourceResponse getSource(String treeId, String sourceId) {
        treeService.ensureTreeExists(treeId);
        sourceService.ensureSourceExists(sourceId);
        return sourceRepository.findByTreeIdAndSourceId(treeId, sourceId)
                .orElseThrow(() -> new NodeNotFoundException("Source " + sourceId.toString() + " not found for tree " + treeId.toString()));
    }
}
