package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.file.dto.params.DeleteFileParams;
import com.ada.genealogyapp.file.dto.params.SaveFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileParams;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;

import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileDataManager implements FileService {

    private final FileRepository fileRepository;

    private final QueryResultProcessor processor;

    @TransactionalInNeo4j
    public void saveFile(SaveFileParams params) {
        String result = fileRepository.save(params.getUserId(), params.getTreeId(), params.getFileId(), params.getFile().getName(), params.getFile().getType(), params.getFile().getPath(), params.getFile().getFilename());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.FILE_ID, params.getFileId()));
    }

    @TransactionalInNeo4j
    public void updateFile(UpdateFileParams params) {
        String result = fileRepository.update(params.getUserId(), params.getTreeId(), params.getFileId(), params.getFile().getName());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.FILE_ID, params.getFileId()));
    }

    @TransactionalInNeo4j
    public void deleteFile(DeleteFileParams params) {
        String result = fileRepository.delete(params.getUserId(), params.getTreeId(), params.getFileId());
        processor.process(result, Map.of(IdType.FILE_ID,  params.getFileId()));
    }
}
