package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SourceCreationService {

    private final SourceRepository sourceRepository;

    private final TreeService treeService;


    public SourceCreationService(SourceRepository sourceRepository, TreeService treeService) {
        this.sourceRepository = sourceRepository;
        this.treeService = treeService;
    }

    public Source createSource(UUID treeId, SourceRequest sourceRequest) {
        Source source = SourceRequest.dtoToEntityMapper().apply(sourceRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);

        source.setTree(tree);

        sourceRepository.save(source);
        treeService.saveTree(tree);

        log.info("Source created successfully: {}", source.getName());

        return source;
    }
}
