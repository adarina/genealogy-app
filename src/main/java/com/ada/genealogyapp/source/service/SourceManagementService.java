package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SourceManagementService {

    private final TreeService treeService;

    private final SourceService sourceService;

    private final SourceRepository sourceRepository;

    public SourceManagementService(TreeService treeService, SourceService sourceService, SourceRepository sourceRepository) {
        this.treeService = treeService;
        this.sourceService = sourceService;
        this.sourceRepository = sourceRepository;
    }


    //TODO validation
    @TransactionalInNeo4j
    public void updateSource(UUID treeId, UUID sourceId, SourceRequest sourceRequest) {
        treeService.ensureTreeExists(treeId);
        sourceService.ensureSourceExists(sourceId);

        sourceRepository.updateSource(sourceId, sourceRequest.getName());
        log.info("Source updated successfully: {}", sourceId);
    }
}
