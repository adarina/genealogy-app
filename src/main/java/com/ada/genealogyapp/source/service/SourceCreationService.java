package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SourceCreationService {

    private final SourceService sourceService;

    public SourceCreationService(SourceService sourceService) {
        this.sourceService = sourceService;
    }


    @TransactionalInNeo4j
    public Source createSource(String userId, String treeId, SourceRequest sourceRequest) {
        Source source = Source.builder()
                .id(UUID.randomUUID().toString())
                .name(sourceRequest.getName())
                .build();

        //TODO validation
        sourceService.saveSource(userId, treeId, source);
        return source;
    }

    @TransactionalInNeo4j
    public Source createSource(String userId, Tree tree, String name) {
        Source source = Source.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .build();

        //TODO validation
        sourceService.saveSource(userId, tree.getId(), source);
        return source;
    }
}
