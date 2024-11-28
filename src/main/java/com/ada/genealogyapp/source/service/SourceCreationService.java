package com.ada.genealogyapp.source.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SourceCreationService {

    private final SourceService sourceService;

    private final TreeService treeService;

    public SourceCreationService(SourceService sourceService, TreeService treeService) {
        this.sourceService = sourceService;
        this.treeService = treeService;
    }


    //TODO validation
    @TransactionalInNeo4j
    public Source createSource(String treeId, SourceRequest sourceRequest) {
        Tree tree = treeService.findTreeById(treeId);
        Source source = Source.builder()
                .tree(tree)
                .name(sourceRequest.getName())
                .build();

        sourceService.saveSource(source);
        return source;
    }
}
