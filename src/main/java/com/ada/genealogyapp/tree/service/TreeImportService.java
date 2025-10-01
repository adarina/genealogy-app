package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.graphuser.dto.GraphUserResponse;
import com.ada.genealogyapp.graphuser.service.GraphUserViewService;
import com.ada.genealogyapp.tree.dto.params.CreateTreeImportParams;
import com.ada.genealogyapp.tree.model.Tree;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@EqualsAndHashCode
@AllArgsConstructor
public abstract class TreeImportService<T, P> {

    private final GraphUserViewService graphUserViewService;

    private final TreeCreationService treeCreationService;

    @Transactional(rollbackOn = Exception.class)
    public Tree importTreeFile(MultipartFile multipartFile, String userId) throws IOException {
        if (isNull(multipartFile)) {
            throw new IllegalArgumentException("MultipartFile cannot be null");
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            T importRequest = parseInputFile(inputStream);
            String treeName = determineTreeName(multipartFile);
            GraphUserResponse graphUser = graphUserViewService.getGraphUser(userId);
            Tree tree = createTree(graphUser.getId(), treeName);
            P params = initializeImportParams(tree, userId);
            processEntities(importRequest, params);
            return tree;
        } catch (IOException | SAXParseException ex) {
            throw new IOException("Failed to process file", ex);
        }
    }


    @Transactional(rollbackOn = Exception.class)
    protected Tree createTree(String userId, String treeName) {
        return treeCreationService.createTreeImport(CreateTreeImportParams.builder()
                .userId(userId)
                .name(treeName)
                .build());
    }

    private String determineTreeName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        return nonNull(originalFilename)
                ? originalFilename.replaceFirst("\\.[^.]*$", "")
                : "imported-tree-" + System.nanoTime();
    }

    protected abstract T parseInputFile(InputStream inputStream) throws IOException, SAXParseException;

    protected abstract P initializeImportParams(Tree tree, String userId);

    protected abstract void processEntities(T importRequest, P params);

    protected void processAllEntities(T request, P params, List<EntityProcessor<T, P>> processors) {
        for (EntityProcessor<T, P> processor : processors) {
            processor.process(request, params);
        }
    }

    protected abstract void processSources(T importRequest, P params);

    protected abstract void processFiles(T importRequest, P params);

    protected abstract void processPersons(T importRequest, P params);

    protected abstract void processFamilies(T importRequest, P params);
}
