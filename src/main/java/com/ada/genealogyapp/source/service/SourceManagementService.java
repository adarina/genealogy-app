package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void updateSource(String treeId, String sourceId, SourceRequest sourceRequest) {
        treeService.ensureTreeExists(treeId);
        Source source = sourceService.findSourceById(sourceId);

        source.setName(sourceRequest.getName());

        sourceService.saveSource(source);
    }
}
