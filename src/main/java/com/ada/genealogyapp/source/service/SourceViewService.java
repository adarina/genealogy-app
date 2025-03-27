package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.source.dto.SourceFilterRequest;
import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.dto.params.GetSourceParams;
import com.ada.genealogyapp.source.dto.params.GetSourcesParams;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SourceViewService {

    private final TreeService treeService;

    private final ObjectMapper objectMapper;

    private final SourceRepository sourceRepository;

    public Page<SourceResponse> getSources(GetSourcesParams params) throws JsonProcessingException {
        SourceFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), SourceFilterRequest.class);
        Page<SourceResponse> page = sourceRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getName(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public SourceResponse getSource(GetSourceParams params) {
        SourceResponse sourceResponse = sourceRepository.find(params.getUserId(), params.getTreeId(), params.getSourceId());
        treeService.ensureUserAndTreeExist(params, sourceResponse);
        return sourceResponse;
    }
}