package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileCreationService {

    private final FileService fileService;

    @TransactionalInNeo4j
    public File createFile(String userId, Tree tree, String name, String type, String path) {
        File file = File.builder()
                .id(UUID.randomUUID().toString())
                .filename(name)
                .name(name)
                .path(path)
                .type(type)
                .tree(tree)
                .build();

        //TODO validation
        fileService.saveFile(userId, tree.getId(), file);
        return file;
    }
}
