package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final QueryResultProcessor queryResultProcessor;

    public FileServiceImpl(FileRepository fileRepository, QueryResultProcessor queryResultProcessor) {
        this.fileRepository = fileRepository;
        this.queryResultProcessor = queryResultProcessor;
    }



    public void ensureFileExists(String fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new NodeNotFoundException("File not found with ID: " + fileId);
        }
    }

//    @TransactionalInNeo4j
//    public void saveFile(File file) {
//        File savedFile = fileRepository.save(file);
//        log.info("File saved successfully: {}", savedFile);
//    }

    @TransactionalInNeo4j
    public void saveFile(String treeId, @NonNull File file) {
        String result = fileRepository.save(treeId, file.getId(), file.getName(), file.getType(), file.getPath(), file.getFilename());
        queryResultProcessor.process(result, Map.of("treeId", treeId, "fileId", file.getId()));
    }
}
