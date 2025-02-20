package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SourceManagementService {


    private final SourceService sourceService;


    public SourceManagementService(SourceService sourceService) {
        this.sourceService = sourceService;
    }


    @TransactionalInNeo4j
    public void updateSource(String userId, String treeId, String sourceId, SourceRequest sourceRequest) {
        Source source = Source.builder()
                .name(sourceRequest.getName())
                .build();

        //TODO validation
        sourceService.updateSource(userId, treeId, sourceId, source);
    }

    //TODO deletion
}
