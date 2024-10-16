package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SourceCreationService {

    private final SourceRepository sourceRepository;

    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    public SourceCreationService(SourceRepository sourceRepository, TreeSearchService treeSearchService, TreeRepository treeRepository) {
        this.sourceRepository = sourceRepository;
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
    }

    public Source create(Source source) {
        Source savedSource = sourceRepository.save(source);
        log.info("Source created successfully: {}", savedSource);
        return savedSource;
    }

    public void createSource(UUID treeId, SourceRequest sourceRequest) {
        Source source = SourceRequest.dtoToEntityMapper().apply(sourceRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        source.setTree(tree);

        create(source);
        treeRepository.save(tree);

        log.info("Source created successfully: {}", source.getName());
    }
}
